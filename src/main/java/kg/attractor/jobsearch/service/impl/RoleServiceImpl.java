package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.exception.ResourceNotFoundException;
import kg.attractor.jobsearch.model.Role;
import kg.attractor.jobsearch.repository.RoleRepository;
import kg.attractor.jobsearch.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Роль " + name + " не найдена"));
    }
}
