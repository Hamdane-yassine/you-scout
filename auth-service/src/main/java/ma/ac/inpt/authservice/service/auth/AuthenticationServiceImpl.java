package ma.ac.inpt.authservice.service.auth;

import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.dto.AuthenticationRequest;
import ma.ac.inpt.authservice.dto.AuthenticationResponse;
import ma.ac.inpt.authservice.dto.EmailVerificationType;
import ma.ac.inpt.authservice.exception.auth.AccountNotEnabledException;
import ma.ac.inpt.authservice.exception.auth.AuthenticationFailedException;
import ma.ac.inpt.authservice.exception.auth.InvalidRefreshTokenException;
import ma.ac.inpt.authservice.model.RefreshToken;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.repository.RefreshTokenRepository;
import ma.ac.inpt.authservice.repository.UserRepository;
import ma.ac.inpt.authservice.service.oauth2.OAuth2Provider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service implementation for handling authentication functionality.
 */
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtEncoder jwtEncoder; // Component for encoding JWT tokens
    private final JwtDecoder jwtDecoder; // Component for decoding JWT tokens
    private final AuthenticationManager authenticationManager; // Spring's authentication manager
    private final UserRepository userRepository; // Custom user details service for loading user data
    private final EmailVerificationService emailVerificationService; // Service for handling account verification
    private final Map<String, OAuth2Provider> oAuth2Providers; // Map of supported OAuth2 providers
    private final RefreshTokenRepository refreshTokenRepository;
    private final static Integer REFRESH_TOKEN_EXPIRE_DATE_IN_DAYS = 7;
    private final static Integer ACCESS_TOKEN_EXPIRE_DATE_IN_MINUTES = 15;


    public AuthenticationServiceImpl(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, AuthenticationManager authenticationManager, UserRepository userRepository, EmailVerificationService emailVerificationService, List<OAuth2Provider> oAuth2Providers, RefreshTokenRepository refreshTokenRepository) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.emailVerificationService = emailVerificationService;
        this.oAuth2Providers = oAuth2Providers.stream().collect(Collectors.toMap(OAuth2Provider::getName, Function.identity()));
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * Authenticates the user based on the provided authentication request.
     *
     * @param request the authentication request containing the user's credentials
     * @return the authentication response containing the generated access token and optional refresh token
     */
    @Override
    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Authenticating user with grant type: {}", request.getGrantType().toUpperCase());
        String grantType = request.getGrantType().toUpperCase();
        if (grantType.equals("PASSWORD")) {
            return authenticatePasswordGrant(request);
        } else if (grantType.equals("REFRESH_TOKEN")) {
            return authenticateRefreshTokenGrant(request);
        }
        throw new IllegalArgumentException("Unsupported grant type : " + grantType);
    }

    /**
     * Authenticates the user using OAuth2 with the specified provider and authorization code.
     *
     * @param provider          the OAuth2 provider to use for authentication
     * @param authorizationCode the authorization code provided by the OAuth2 provider
     * @return the authentication response containing the generated access token and optional refresh token
     */
    @Override
    @Transactional
    public AuthenticationResponse authenticateOAuth2(String provider, String authorizationCode) {
        OAuth2Provider oAuth2Provider = oAuth2Providers.get(provider);
        if (oAuth2Provider == null) {
            throw new IllegalArgumentException("Unsupported OAuth2 provider: " + provider);
        }
        var authenticationRequest = oAuth2Provider.authenticate(authorizationCode);
        User user = userRepository.findByUsernameIgnoreCase(authenticationRequest.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        String scope = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        return buildAuthenticationResponse(authenticationRequest.isWithRefreshToken(), authenticationRequest.getUsername(), scope);
    }

    /**
     * Logs out the user with the given username.
     *
     * @param username the username of the user to be logged out
     */
    @Override
    @Transactional
    public void logout(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        log.info("Username is {}", user.getUsername());
        user.setRefreshToken(null);
        userRepository.save(user);
    }


    /**
     * Authenticates the user using password grant type.
     *
     * @param request the authentication request containing the user's credentials
     * @return the authentication response containing the generated access token and optional refresh token
     */
    private AuthenticationResponse authenticatePasswordGrant(AuthenticationRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            String subject = authentication.getName();
            String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
            log.info("Password grant authentication successful for user: {}", subject);
            return buildAuthenticationResponse(request.isWithRefreshToken(), subject, scope);
        } catch (BadCredentialsException e) {
            throw new AuthenticationFailedException("Username or password Incorrect");
        }
        catch (DisabledException e) {
            User user = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            String message = emailVerificationService.sendVerificationEmail(user, EmailVerificationType.RESEND);
            throw new AccountNotEnabledException(message);
        }
    }

    /**
     * Authenticates the user using refresh token grant type.
     *
     * @param request the authentication request containing the user's refresh token
     * @return the authentication response containing the generated access token and optional refresh token
     */
    private AuthenticationResponse authenticateRefreshTokenGrant(AuthenticationRequest request) {
        Jwt decodeJWT;
        try {
            decodeJWT = jwtDecoder.decode(request.getRefreshToken());
        } catch (JwtException e) {
            String errorMessage = "Error decoding refresh token";
            log.error(errorMessage, e);
            throw new InvalidRefreshTokenException(errorMessage);
        }
        String jti = decodeJWT.getClaim("jti");
        if (!refreshTokenRepository.existsByTokenUuid(jti))
            throw new InvalidRefreshTokenException("Error validating refresh token");
        String subject = decodeJWT.getSubject();
        User user = userRepository.findByUsernameIgnoreCase(subject).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        String scope = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        log.info("Refresh token grant authentication successful for user: {}", subject);
        return buildAuthenticationResponse(request.isWithRefreshToken(), subject, scope);
    }

    /**
     * Builds an authentication response containing the generated access token and optional refresh token.
     *
     * @param withRefreshToken whether a refresh token should be included in the response
     * @param subject          the subject of the JWT token (the user's username)
     * @param scope            the scope of the JWT token (the user's granted authorities)
     * @return the authentication response containing the generated access token and optional refresh token
     */
    private AuthenticationResponse buildAuthenticationResponse(boolean withRefreshToken, String subject, String scope) {
        Instant instant = Instant.now();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder().subject(subject).issuedAt(instant).expiresAt(instant.plus(ACCESS_TOKEN_EXPIRE_DATE_IN_MINUTES, ChronoUnit.MINUTES)).issuer("you-scout-auth-service").claim("scope", scope).build();
        String jwtAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        Optional<String> jwtRefreshToken = Optional.empty();

        if (withRefreshToken) {
            String refreshTokenUuid = UUID.randomUUID().toString();
            jwtRefreshToken = Optional.of(jwtEncoder.encode(JwtEncoderParameters.from(JwtClaimsSet.builder().subject(subject).issuedAt(instant).expiresAt(instant.plus(REFRESH_TOKEN_EXPIRE_DATE_IN_DAYS, ChronoUnit.DAYS)).issuer("you-scout-auth-service").claim("jti", refreshTokenUuid).build())).getTokenValue());

            // Retrieve the user from the subject (username)
            User user = userRepository.findByUsernameIgnoreCase(subject).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (user.getRefreshToken() != null) {
                // Update the existing refresh token
                user.getRefreshToken().setTokenUuid(refreshTokenUuid);
                user.getRefreshToken().setExpiryDate(instant.plus(REFRESH_TOKEN_EXPIRE_DATE_IN_DAYS, ChronoUnit.DAYS));

                // Save the updated refresh token
                userRepository.save(user);
            } else {
                // If there's no existing refresh token, create a new one
                RefreshToken refreshToken = RefreshToken.builder()
                        .tokenUuid(refreshTokenUuid)
                        .expiryDate(instant.plus(REFRESH_TOKEN_EXPIRE_DATE_IN_DAYS, ChronoUnit.DAYS))
                        .build();

                user.setRefreshToken(refreshToken);
                // Save the new refresh token to the database
                userRepository.save(user);
            }

        }

        log.info("Authentication response generated for user: {}", subject);
        return AuthenticationResponse.builder().accessToken(jwtAccessToken).refreshToken(jwtRefreshToken.orElse(null)).build();
    }
}

