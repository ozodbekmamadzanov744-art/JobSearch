package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.exception.ResourceNotFoundException;
import kg.attractor.jobsearch.model.Role;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.repository.RoleRepository;
import kg.attractor.jobsearch.repository.UserRepository;
import kg.attractor.jobsearch.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void assignRole(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + userId + " не найден"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Роль с id " + roleId + " не найдена"));

        user.getRoles().add(role);
        userRepository.save(user);
    }
}
