package kg.attractor.jobsearch.dao;

public interface UserRoleDao {

    void assignRole(Long userId, Long roleId);
}