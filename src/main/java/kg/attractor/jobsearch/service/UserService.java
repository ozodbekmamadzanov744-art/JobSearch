package kg.attractor.jobsearch.service;

import kg.attractor.jobsearch.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    User register(User user);

    User getUserById(Long id);

    List<User> findByName(String name);

    User findByPhoneNumber(String phoneNumber);

    User findByEmail(String email);

    void uploadAvatar(Long id, MultipartFile file);
}