package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.dao.ResumeDao;
import kg.attractor.jobsearch.model.Resume;
import kg.attractor.jobsearch.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeDao resumeDao;

    @Override
    public Resume createResume(Resume resume) {
        if (resume.getIsActive() == null) {
            resume.setIsActive(true);
        }
        return resumeDao.save(resume);
    }

    @Override
    public Resume updateResume(Long id, Resume resume) {
        Resume existing = getResumeById(id);
        resume.setId(existing.getId());
        resume.setApplicantId(existing.getApplicantId());
        resumeDao.update(resume);
        return resume;
    }

    @Override
    public void deleteResume(Long id) {
        getResumeById(id);
        resumeDao.delete(id);
    }

    @Override
    public Resume getResumeById(Long id) {
        return resumeDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Резюме с id " + id + " не найдено"));
    }

    @Override
    public List<Resume> getAllActiveResumes() {
        return resumeDao.findAll().stream()
                .filter(resume -> Boolean.TRUE.equals(resume.getIsActive()))
                .toList();
    }

    @Override
    public List<Resume> getResumesByCategory(Long categoryId) {
        return resumeDao.findByCategoryId(categoryId).stream()
                .filter(resume -> Boolean.TRUE.equals(resume.getIsActive()))
                .toList();
    }

    @Override
    public List<Resume> getResumesByApplicant(Long applicantId) {
        return resumeDao.findByApplicantId(applicantId);
    }
}