package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.user.passwordreset.PasswordResetRequest;
import com.dreamteam.eduuca.entities.user.User;
import com.dreamteam.eduuca.payload.request.UserDataRequest;
import com.dreamteam.eduuca.repositories.PasswordResetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.mail.EmailException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Log4j2
@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private static final Executor passwordResetExecutor = Executors.newSingleThreadExecutor(r -> new Thread(r, "passwordResetExecutor"));

    private final UserService userService;
    private final PasswordResetRepository passwordResetRepository;
    private final MailService mailService;

    public void createPasswordResetRequestFor(String username) {
        log.debug("createPasswordResetRequestFor() called. Username: {}", () -> username);
        User requiredUser = userService.loadUserByUsername(username);
        passwordResetRepository.save(new PasswordResetRequest(requiredUser));
        log.trace("createPasswordResetRequestFor(). Password reset request for {} saved successfully", () -> username);
    }

    public String sendEmailWithCodeTo(String username) {
        log.debug("sendEmailWithCodeTo() called. Username: {}", () -> username);
        User requiredUser = userService.loadUserByUsername(username);

        String code = passwordResetRepository
                .findByUser_Username(username)
                .orElseThrow(IllegalArgumentException::new)
                .getId()
                .toString();

        log.trace("sendEmailWithCodeTo(). Password reset request ID: {}", code);

        String email = requiredUser.getEmail();

        passwordResetExecutor.execute(() -> {
            try {
                mailService.sendPasswordChangeCode(email, code);
            } catch (EmailException e) {
                throw new RuntimeException(e);
            }
        });

        return hideEmail(email);
    }

    private String hideEmail(String email) {
        StringBuilder emailAnswer = new StringBuilder();
        String emailName = email.split("@")[0];
        String emailDomain = email.split("@")[1];
        emailAnswer.append(emailName.charAt(0));
        emailAnswer.append("*".repeat(emailName.length()));
        emailAnswer.append(emailDomain);
        return emailAnswer.toString();
    }

    public void setNewPassword(UUID requestId, String newPassword) {
        log.debug("setNewPassword() called. Request ID: {}", requestId);
        Optional<PasswordResetRequest> passwordResetRequestOpt = passwordResetRepository.findById(requestId);

        if (passwordResetRequestOpt.isEmpty()) {
            log.warn("setNewPassword(). Password reset request does not exist");
            throw new IllegalStateException("Password reset request does not exist");
        }

        PasswordResetRequest passwordResetRequest = passwordResetRequestOpt.get();

        if (!passwordResetRequest.isValid()) {
            log.warn("setNewPassword(). Password reset request is invalid");
            throw new IllegalStateException("Password reset request is invalid");
        }

        UserDataRequest newUserData = new UserDataRequest();
        newUserData.setPassword(Optional.ofNullable(newPassword));

        UUID userId = userService.loadUserByUsername(passwordResetRequest.getUser().getUsername()).getId();

        passwordResetRepository.delete(passwordResetRequest);
        log.trace("setNewPassword(). Password reset request is completed and deleted");

        userService.updateUser(userId, newUserData);
        log.trace("setNewPassword(). Password of user {} updated", userId);
    }
}
