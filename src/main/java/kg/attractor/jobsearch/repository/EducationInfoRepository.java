package kg.attractor.jobsearch.repository;

import kg.attractor.jobsearch.model.EducationInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationInfoRepository extends JpaRepository<EducationInfo, Long> {
}