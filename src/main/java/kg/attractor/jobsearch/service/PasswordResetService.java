package kg.attractor.jobsearch.service;

import jakarta.mail.MessagingException;
import kg.attractor.jobsearch.model.User;

import java.io.UnsupportedEncodingException;

public interface PasswordResetService {

    /**
     * Генерирует одноразовый токен восстановления пароля для пользователя с указанным email,
     * сохраняет его и отправляет пользователю письмо со ссылкой для сброса пароля.
     *
     * @param email   email пользователя, запросившего восстановление пароля
     * @param siteUrl базовый адрес сайта (используется для формирования ссылки в письме)
     */
    void createResetToken(String email, String siteUrl) throws MessagingException, UnsupportedEncodingException;

    /**
     * Находит пользователя по токену восстановления пароля.
     * Бросает ResourceNotFoundException, если токен недействителен или уже был использован.
     */
    User getByResetPasswordToken(String token);

    /**
     * Устанавливает пользователю новый пароль и аннулирует токен восстановления.
     */
    void resetPassword(User user, String newPassword);
}
