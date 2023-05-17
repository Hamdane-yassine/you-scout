package ma.ac.inpt.authservice.repository;

import ma.ac.inpt.authservice.model.PasswordResetToken;

/**
 * Repository for managing PasswordResetToken entities.
 * Extends TokenRepository for common token management functionality.
 */
public interface PasswordResetTokenRepository extends TokenRepository<PasswordResetToken> {
}