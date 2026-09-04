package kg.attractor.jobsearch.service;

import jakarta.mail.MessagingException;
import kg.attractor.jobsearch.model.User;

import java.io.UnsupportedEncodingException;

public interface PasswordResetService {

    void createResetToken(String email, String siteUrl) throws MessagingException, UnsupportedEncodingException;

    User getByResetPasswordToken(String token);

    void resetPassword(User user, String newPassword);
}
