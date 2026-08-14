package kg.attractor.jobsearch.repository;

import kg.attractor.jobsearch.model.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactTypeRepository extends JpaRepository<ContactType, Long> {
}