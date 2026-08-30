package kg.attractor.jobsearch.repository;

import kg.attractor.jobsearch.model.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {

    List<ContactInfo> findByResumeId(Long resumeId);

    void deleteByResumeId(Long resumeId);
}
