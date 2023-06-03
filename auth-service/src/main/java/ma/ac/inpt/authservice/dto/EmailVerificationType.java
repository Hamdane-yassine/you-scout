package ma.ac.inpt.authservice.dto;

/**
 * Enum representing the types of email verifications.
 * Possible values are PASSWORD_RESET, REGISTRATION, UPDATING, and RESEND.
 */
public enum EmailVerificationType {
    PASSWORD_RESET,    // Email verification for password reset
    REGISTRATION,      // Email verification for registration
    UPDATING,          // Email verification for updating user information
    RESEND             // Email verification for resending verification
}
