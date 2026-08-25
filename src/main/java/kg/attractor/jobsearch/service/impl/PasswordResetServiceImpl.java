package kg.attractor.jobsearch.service.impl;

import jakarta.mail.MessagingException;
import kg.attractor.jobsearch.exception.ResourceNotFoundException;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.repository.UserRepository;
import kg.attractor.jobsearch.service.EmailService;
import kg.attractor.jobsearch.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final String RESET_PASSWORD_PATH = "/pages/auth/reset-password?token=";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    @Transactional
    public void createResetToken(String email, String siteUrl) throws MessagingException, UnsupportedEncodingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с email " + email + " не найден"));

        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        userRepository.save(user);

        String resetLink = siteUrl + RESET_PASSWORD_PATH + token;
        emailService.sendResetPasswordEmail(email, resetLink);

        log.info("Создан токен восстановления пароля для пользователя с email {}", email);
    }

    @Override
    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ссылка для восстановления пароля недействительна или уже была использована"));
    }

    @Override
    @Transactional
    public void resetPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        userRepository.save(user);
        log.info("Пароль пользователя id={} успешно изменён через восстановление", user.getId());
    }
}
