package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.exception.EmailAlreadyExistsException;
import kg.attractor.jobsearch.exception.ForbiddenOperationException;
import kg.attractor.jobsearch.exception.ResourceNotFoundException;
import kg.attractor.jobsearch.model.Role;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.repository.UserRepository;
import kg.attractor.jobsearch.service.RoleService;
import kg.attractor.jobsearch.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_AVATAR = "/images/default-avatar.png";
    private static final String UPLOAD_DIR = "uploads/avatars";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

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

        String requestedRole = user.getAccountType();
        User saved = userRepository.save(user);

        Role role = roleService.getRoleByName(requestedRole);
        saved.getRoles().add(role);
        userRepository.save(saved);
        saved.setAccountType(role.getName());

        log.info("Пользователь id={} успешно зарегистрирован с ролью {}", saved.getId(), role.getName());
        return saved;
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + id + " не найден"));
        populateAccountType(user);
        return user;
    }

    @Override
    public List<User> findByName(String name) {
        List<User> users = userRepository.findByName(name);
        users.forEach(this::populateAccountType);
        return users;
    }

    @Override
    public User findByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с телефоном " + phoneNumber + " не найден"));
        populateAccountType(user);
        return user;
    }

    @Override
    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с email " + email + " не найден"));
        populateAccountType(user);
        return user;
    }

    @Override
    public void uploadAvatar(Long id, MultipartFile file, Long currentUserId) {
        if (!id.equals(currentUserId)) {
            throw new ForbiddenOperationException("Нельзя загрузить аватар другому пользователю");
        }

        log.info("Загрузка аватара для пользователя id={}", id);
        User user = getUserById(id);

        if (file == null || file.isEmpty()) {
            user.setAvatar(DEFAULT_AVATAR);
            userRepository.save(user);
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
            userRepository.save(user);
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
        return userRepository.existsByEmail(email);
    }

    @Override
    public User updateProfile(Long id, User updates, Long currentUserId) {
        if (!id.equals(currentUserId)) {
            throw new ForbiddenOperationException("Нельзя редактировать профиль другого пользователя");
        }

        log.info("Обновление профиля пользователя id={}", id);
        User existing = getUserById(id);
        existing.setName(updates.getName());
        existing.setSurname(updates.getSurname());
        existing.setAge(updates.getAge());
        existing.setPhoneNumber(updates.getPhoneNumber());
        userRepository.save(existing);
        return existing;
    }

    @Override
    public Page<User> getEmployers(int page, int size) {
        Page<User> employers = userRepository.findByRoles_Name("EMPLOYER", PageRequest.of(page, size));
        employers.forEach(this::populateAccountType);
        return employers;
    }

    private void populateAccountType(User user) {
        user.getRoles().stream()
                .findFirst()
                .ifPresent(role -> user.setAccountType(role.getName()));
    }
}
