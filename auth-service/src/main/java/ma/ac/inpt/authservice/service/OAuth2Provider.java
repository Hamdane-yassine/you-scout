package ma.ac.inpt.authservice.service;

import ma.ac.inpt.authservice.payload.AuthenticationRequest;

import java.io.IOException;

public interface OAuth2Provider {
    String getName();

    AuthenticationRequest authenticate(String authorizationCode) throws IOException;
}
