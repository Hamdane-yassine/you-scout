package ma.ac.inpt.authservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.payload.ForgotPasswordRequest;
import ma.ac.inpt.authservice.payload.ResetPasswordRequest;
import ma.ac.inpt.authservice.service.AccountVerificationService;
import ma.ac.inpt.authservice.service.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    private final AccountVerificationService accountVerificationService;

    private final PasswordResetService passwordResetService;

    @GetMapping("/confirm-account")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) {
        String message = accountVerificationService.verifyAccount(token);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        String message = passwordResetService.sendPasswordResetEmail(request);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request, @RequestParam String token) {
        String message = passwordResetService.resetPassword(request, token);
        return ResponseEntity.ok(message);
    }

}
