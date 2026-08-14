package kg.attractor.jobsearch.repository;

import kg.attractor.jobsearch.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}