package kg.attractor.jobsearch.repository;

import kg.attractor.jobsearch.model.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    Page<Resume> findByIsActiveTrue(Pageable pageable);

    Page<Resume> findByApplicantId(Long applicantId, Pageable pageable);

    List<Resume> findByCategoryId(Long categoryId);
}