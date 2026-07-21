package kg.attractor.jobsearch.dao;

import kg.attractor.jobsearch.model.Resume;

import java.util.List;
import java.util.Optional;

public interface ResumeDao {

    Resume save(Resume resume);

    void update(Resume resume);

    void delete(Long id);

    Optional<Resume> findById(Long id);

    List<Resume> findAll();

    List<Resume> findByApplicantId(Long applicantId);

    List<Resume> findByCategoryId(Long categoryId);
}