package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.dao.UserRoleDao;
import kg.attractor.jobsearch.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleDao userRoleDao;

    @Override
    public void assignRole(Long userId, Long roleId) {
        userRoleDao.assignRole(userId, roleId);
    }
}