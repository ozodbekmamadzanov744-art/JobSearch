package kg.attractor.jobsearch.repository;

import kg.attractor.jobsearch.model.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {
}