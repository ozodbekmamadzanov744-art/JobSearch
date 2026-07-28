package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.dao.UserDao;
import kg.attractor.jobsearch.exception.EmailAlreadyExistsException;
import kg.attractor.jobsearch.exception.ResourceNotFoundException;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_AVATAR = "/images/default-avatar.png";
    private static final String UPLOAD_DIR = "uploads/avatars";

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User register(User user) {
        log.info("Регистрация нового пользователя с email {}", user.getEmail());

        if (existsByEmail(user.getEmail())) {
            log.warn("Попытка регистрации с уже занятым email {}", user.getEmail());
            throw new EmailAlreadyExistsException(
                    "Пользователь с email " + user.getEmail() + " уже зарегистрирован");
        }

        if (user.getAvatar() == null || user.getAvatar().isBlank()) {
            user.setAvatar(DEFAULT_AVATAR);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User saved = userDao.save(user);
        log.info("Пользователь id={} успешно зарегистрирован", saved.getId());
        return saved;
    }

    @Override
    public User getUserById(Long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " не найден"));
    }

    @Override
    public List<User> findByName(String name) {
        return userDao.findByName(name);
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        return userDao.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с телефоном " + phoneNumber + " не найден"));
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с email " + email + " не найден"));
    }

    @Override
    public void uploadAvatar(Long id, MultipartFile file) {
        log.info("Загрузка аватара для пользователя id={}", id);
        User user = getUserById(id);

        if (file == null || file.isEmpty()) {
            user.setAvatar(DEFAULT_AVATAR);
            userDao.update(user);
            return;
        }

        try {
            Path uploadPath = Path.of(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String extension = getExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID() + extension;
            Path targetPath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            user.setAvatar("/" + UPLOAD_DIR + "/" + fileName);
            userDao.update(user);
            log.info("Аватар пользователя id={} обновлён: {}", id, user.getAvatar());
        } catch (IOException e) {
            log.error("Ошибка при сохранении файла аватара для пользователя id={}", id, e);
            throw new UncheckedIOException("Ошибка при сохранении файла аватара", e);
        }
    }

    private String getExtension(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "";
        }
        return originalFilename.substring(originalFilename.lastIndexOf('.'));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userDao.existsByEmail(email);
    }

    @Override
    public User updateProfile(Long id, User updates) {
        log.info("Обновление профиля пользователя id={}", id);
        User existing = getUserById(id);
        existing.setName(updates.getName());
        existing.setSurname(updates.getSurname());
        existing.setAge(updates.getAge());
        existing.setPhoneNumber(updates.getPhoneNumber());
        userDao.update(existing);
        return existing;
    }
}