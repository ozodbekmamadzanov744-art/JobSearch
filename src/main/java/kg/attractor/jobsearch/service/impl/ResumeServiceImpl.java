package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.dao.EducationInfoDao;
import kg.attractor.jobsearch.dao.ResumeDao;
import kg.attractor.jobsearch.dao.WorkExperienceInfoDao;
import kg.attractor.jobsearch.model.EducationInfo;
import kg.attractor.jobsearch.model.Resume;
import kg.attractor.jobsearch.model.WorkExperienceInfo;
import kg.attractor.jobsearch.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeDao resumeDao;
    private final EducationInfoDao educationInfoDao;
    private final WorkExperienceInfoDao workExperienceInfoDao;

    @Override
    public Resume createResume(Resume resume, List<EducationInfo> educationList, List<WorkExperienceInfo> workExperienceList) {
        if (resume.getIsActive() == null) {
            resume.setIsActive(true);
        }

        Resume saved = resumeDao.save(resume);

        saveEducation(saved.getId(), educationList);
        saveWorkExperience(saved.getId(), workExperienceList);

        return saved;
    }

    @Override
    public Resume updateResume(Long id, Resume resume, List<EducationInfo> educationList, List<WorkExperienceInfo> workExperienceList) {
        Resume existing = getResumeById(id);
        resume.setId(existing.getId());
        resume.setApplicantId(existing.getApplicantId());
        resumeDao.update(resume);

        educationInfoDao.deleteByResumeId(id);
        saveEducation(id, educationList);

        workExperienceInfoDao.deleteByResumeId(id);
        saveWorkExperience(id, workExperienceList);

        return resume;
    }

    @Override
    public void deleteResume(Long id) {
        getResumeById(id);
        educationInfoDao.deleteByResumeId(id);
        workExperienceInfoDao.deleteByResumeId(id);
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

    @Override
    public List<EducationInfo> getEducationByResumeId(Long resumeId) {
        return educationInfoDao.findByResumeId(resumeId);
    }

    @Override
    public List<WorkExperienceInfo> getWorkExperienceByResumeId(Long resumeId) {
        return workExperienceInfoDao.findByResumeId(resumeId);
    }

    private void saveEducation(Long resumeId, List<EducationInfo> educationList) {
        if (educationList == null) {
            return;
        }
        for (EducationInfo education : educationList) {
            education.setResumeId(resumeId);
            educationInfoDao.save(education);
        }
    }

    private void saveWorkExperience(Long resumeId, List<WorkExperienceInfo> workExperienceList) {
        if (workExperienceList == null) {
            return;
        }
        for (WorkExperienceInfo workExperience : workExperienceList) {
            workExperience.setResumeId(resumeId);
            workExperienceInfoDao.save(workExperience);
        }
    }
}