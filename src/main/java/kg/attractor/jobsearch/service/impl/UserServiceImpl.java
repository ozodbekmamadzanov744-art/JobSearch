package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.dao.UserDao;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_AVATAR = "/images/default-avatar.png";
    private static final String UPLOAD_DIR = "uploads/avatars";

    private final UserDao userDao;

    @Override
    public User register(User user) {
        if (existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Пользователь с email " + user.getEmail() + " уже зарегистрирован");
        }

        if (user.getAvatar() == null || user.getAvatar().isBlank()) {
            user.setAvatar(DEFAULT_AVATAR);
        }

        return userDao.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с id " + id + " не найден"));
    }

    @Override
    public List<User> findByName(String name) {
        return userDao.findByName(name);
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        return userDao.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Пользователь с телефоном " + phoneNumber + " не найден"));
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь с email " + email + " не найден"));
    }

    @Override
    public void uploadAvatar(Long id, MultipartFile file) {
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
        } catch (IOException e) {
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
        User existing = getUserById(id);
        existing.setName(updates.getName());
        existing.setSurname(updates.getSurname());
        existing.setAge(updates.getAge());
        existing.setPhoneNumber(updates.getPhoneNumber());
        userDao.update(existing);
        return existing;
    }
}