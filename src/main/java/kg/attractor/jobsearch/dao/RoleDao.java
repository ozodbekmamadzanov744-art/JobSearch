package kg.attractor.jobsearch.dao;

import kg.attractor.jobsearch.model.Role;

import java.util.Optional;

public interface RoleDao {

    Optional<Role> findByName(String name);
}
