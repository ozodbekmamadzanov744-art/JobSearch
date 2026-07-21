package kg.attractor.jobsearch.dao;

import kg.attractor.jobsearch.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    User save(User user);

    void update(User user);

    Optional<User> findById(Long id);

    List<User> findByName(String name);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}