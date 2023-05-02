package ma.ac.inpt.authservice.service.auth;

import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.exception.auth.AccountNotEnabledException;
import ma.ac.inpt.authservice.exception.auth.InvalidRefreshTokenException;
import ma.ac.inpt.authservice.service.oauth2.OAuth2Provider;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.payload.AuthenticationRequest;
import ma.ac.inpt.authservice.payload.AuthenticationResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final UserDetailsService userDetailsService; // Custom user details service for loading user data
    private final AccountVerificationService accountVerificationService; // Service for handling account verification
    private final Map<String, OAuth2Provider> oAuth2Providers; // Map of supported OAuth2 providers

    public AuthenticationServiceImpl(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, AccountVerificationService accountVerificationService, List<OAuth2Provider> oAuth2Providers) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.accountVerificationService = accountVerificationService;
        this.oAuth2Providers = oAuth2Providers.stream().collect(Collectors.toMap(OAuth2Provider::getName, Function.identity()));
    }

    /**
     * Authenticates the user based on the provided authentication request.
     *
     * @param request the authentication request containing the user's credentials
     * @return the authentication response containing the generated access token and optional refresh token
     */
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Authenticating user with grant type: {}", request.getGrantType().toUpperCase());
        String grantType = request.getGrantType().toUpperCase();
        if (grantType.equals("PASSWORD")) {
            return authenticatePasswordGrant(request);
        }
        return authenticateRefreshTokenGrant(request);
    }

    /**
     * Authenticates the user using OAuth2 with the specified provider and authorization code.
     *
     * @param provider          the OAuth2 provider to use for authentication
     * @param authorizationCode the authorization code provided by the OAuth2 provider
     * @return the authentication response containing the generated access token and optional refresh token
     */
    @Override
    public AuthenticationResponse authenticateOAuth2(String provider, String authorizationCode) {
        OAuth2Provider oAuth2Provider = oAuth2Providers.get(provider);
        if (oAuth2Provider == null) {
            throw new IllegalArgumentException("Unsupported OAuth2 provider: " + provider);
        }
        var authenticationRequest = oAuth2Provider.authenticate(authorizationCode);
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        String scope = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        return buildAuthenticationResponse(authenticationRequest.isWithRefreshToken(), authenticationRequest.getUsername(), scope);
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
        } catch (DisabledException e) {
            User user = (User) userDetailsService.loadUserByUsername(request.getUsername());
            String message = accountVerificationService.sendVerificationEmail(user);
            throw new AccountNotEnabledException(message);
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", request.getUsername(), e);
            throw e;
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

        String subject = decodeJWT.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
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
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder().subject(subject).issuedAt(instant).expiresAt(instant.plus(withRefreshToken ? 30 : 55, ChronoUnit.MINUTES)).issuer("you-scout-auth-service").claim("scope", scope).build();
        String jwtAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        Optional<String> jwtRefreshToken = withRefreshToken ? Optional.of(jwtEncoder.encode(JwtEncoderParameters.from(JwtClaimsSet.builder().subject(subject).issuedAt(instant).expiresAt(instant.plus(55, ChronoUnit.MINUTES)).issuer("you-scout-auth-service").build())).getTokenValue()) : Optional.empty();
        log.info("Authentication response generated for user: {}", subject);
        return AuthenticationResponse.builder().accessToken(jwtAccessToken).refreshToken(jwtRefreshToken.orElse(null)).build();
    }
}

