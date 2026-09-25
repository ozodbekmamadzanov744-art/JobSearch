package kg.attractor.jobsearch.service;

import kg.attractor.jobsearch.model.User;

public interface PasswordResetService {

    String createResetToken(String email);

    User getByResetPasswordToken(String token);

    void resetPassword(User user, String newPassword);
}