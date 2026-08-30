package kg.attractor.jobsearch.repository;

import kg.attractor.jobsearch.model.EducationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationInfoRepository extends JpaRepository<EducationInfo, Long> {

    List<EducationInfo> findByResumeId(Long resumeId);

    void deleteByResumeId(Long resumeId);
}
