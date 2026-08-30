package kg.attractor.jobsearch.repository;

import kg.attractor.jobsearch.model.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {

    @Override
    @EntityGraph(attributePaths = {"applicant", "category"})
    Optional<Resume> findById(Long id);

    Page<Resume> findByIsActiveTrue(Pageable pageable);

    Page<Resume> findByApplicantId(Long applicantId, Pageable pageable);

    List<Resume> findByCategoryId(Long categoryId);
}
