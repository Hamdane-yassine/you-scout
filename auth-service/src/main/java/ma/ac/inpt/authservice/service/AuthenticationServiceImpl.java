package ma.ac.inpt.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.exception.InvalidRefreshTokenException;
import ma.ac.inpt.authservice.exception.InvalidRequestException;
import ma.ac.inpt.authservice.payload.AuthenticationRequest;
import ma.ac.inpt.authservice.payload.AuthenticationResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The AuthenticationServiceImpl class is responsible for authenticating users and generating JWT tokens.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    /**
     * JwtEncoder bean to encode JWT tokens.
     */
    private final JwtEncoder jwtEncoder;

    /**
     * JwtDecoder bean to decode JWT tokens.
     */
    private final JwtDecoder jwtDecoder;

    /**
     * AuthenticationManager bean to authenticate user.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * UserDetailsService bean to retrieve user details from database.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Authenticates user and returns authentication response with access token and refresh token (optional).
     *
     * @param request the authentication request.
     * @return the authentication response.
     * @throws InvalidRequestException      if the request is invalid.
     * @throws InvalidRefreshTokenException if the refresh token is invalid.
     */
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Authenticating user with grant type: {}", request.getGrantType().toUpperCase());

        String grantType = request.getGrantType().toUpperCase();

        return switch (grantType) {
            case "PASSWORD" -> authenticatePasswordGrant(request);
            case "REFRESH_TOKEN" -> authenticateRefreshTokenGrant(request);
            default -> {
                String errorMessage = "Invalid grant type: " + request.getGrantType();
                log.error(errorMessage);
                throw new InvalidRequestException(errorMessage);
            }
        };
    }

    /**
     * Authenticates user with password grant type and returns authentication response.
     *
     * @param request the authentication request.
     * @return the authentication response.
     * @throws InvalidRequestException if the request is invalid.
     */
    private AuthenticationResponse authenticatePasswordGrant(AuthenticationRequest request) {
        validateCredentials(request.getUsername(), request.getPassword());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        String subject = authentication.getName();
        String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
        log.info("Password grant authentication successful for user: {}", subject);

        return buildAuthenticationResponse(request.isWithRefreshToken(), subject, scope);
    }

    /**
     * Authenticates user with refresh token grant type and returns authentication response.
     *
     * @param request the authentication request.
     * @return the authentication response.
     * @throws InvalidRequestException      if the request is invalid.
     * @throws InvalidRefreshTokenException if the refresh token is invalid.
     */
    private AuthenticationResponse authenticateRefreshTokenGrant(AuthenticationRequest request) {
        if (request.getRefreshToken() == null) {
            String errorMessage = "Refresh Token is required";
            log.error(errorMessage);
            throw new InvalidRequestException(errorMessage);
        }

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
     * Builds an authentication response.
     *
     * @param withRefreshToken true if a refresh token should be included in the response, false otherwise
     * @param subject          the subject of the JWT
     * @param scope            the scope of the JWT
     * @return the authentication response
     */
    private AuthenticationResponse buildAuthenticationResponse(boolean withRefreshToken, String subject, String scope) {
        Instant instant = Instant.now();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder().subject(subject).issuedAt(instant).expiresAt(instant.plus(withRefreshToken ? 5 : 30, ChronoUnit.MINUTES)).issuer("you-scout-auth-service").claim("scope", scope).build();
        String jwtAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        Optional<String> jwtRefreshToken = withRefreshToken ? Optional.of(jwtEncoder.encode(JwtEncoderParameters.from(JwtClaimsSet.builder().subject(subject).issuedAt(instant).expiresAt(instant.plus(30, ChronoUnit.MINUTES)).issuer("you-scout-auth-service").build())).getTokenValue()) : Optional.empty();
        log.info("Authentication response generated for user: {}", subject);

        return AuthenticationResponse.builder().accessToken(jwtAccessToken).refreshToken(jwtRefreshToken.orElse(null)).build();
    }

    /**
     * This method is used to validate the username and password provided by the user.
     *
     * @param username the username to be validated.
     * @param password the password to be validated.
     * @throws InvalidRequestException if the username or password is null or empty.
     */
    private void validateCredentials(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            String errorMessage = "Password and Username are required";
            log.error(errorMessage);
            throw new InvalidRequestException(errorMessage);
        }
    }
}

