package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.exception.ResourceNotFoundException;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.repository.UserRepository;
import kg.attractor.jobsearch.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public String createResetToken(String email) {
        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() ->
                        new ResourceNotFoundException("error.notFound.user"));

        String token = UUID.randomUUID().toString();

        user.setResetPasswordToken(token);
        userRepository.save(user);

        return token;
    }

    @Override
    public User getByResetPasswordToken(String token) {
        if (token == null || token.isBlank()) {
            throw new ResourceNotFoundException("error.reset.invalidLink");
        }

        return userRepository.findByResetPasswordToken(token.trim())
                .orElseThrow(() ->
                        new ResourceNotFoundException("error.reset.invalidLink"));
    }

    @Override
    @Transactional
    public void resetPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);

        userRepository.save(user);
    }
}