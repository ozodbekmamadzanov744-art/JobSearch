package kg.attractor.jobsearch.repository;

import kg.attractor.jobsearch.model.EducationInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationInfoRepository extends JpaRepository<EducationInfo, Long> {

    List<EducationInfo> findByResumeId(Long resumeId);

    void deleteByResumeId(Long resumeId);
}