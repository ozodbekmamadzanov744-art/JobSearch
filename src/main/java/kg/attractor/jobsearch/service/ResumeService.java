package kg.attractor.jobsearch.service;

import kg.attractor.jobsearch.model.Resume;

import java.util.List;

public interface ResumeService {

    Resume createResume(Resume resume);

    Resume updateResume(Long id, Resume resume);

    void deleteResume(Long id);

    Resume getResumeById(Long id);

    List<Resume> getAllActiveResumes();

    List<Resume> getResumesByCategory(Long categoryId);

    List<Resume> getResumesByApplicant(Long applicantId);
}
