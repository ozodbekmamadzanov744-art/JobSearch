package kg.attractor.jobsearch.repository;

import kg.attractor.jobsearch.model.RespondedApplicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RespondedApplicantRepository extends JpaRepository<RespondedApplicant, Long> {
}