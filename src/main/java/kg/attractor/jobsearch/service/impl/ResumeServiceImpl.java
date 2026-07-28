package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.dao.ResumeDao;
import kg.attractor.jobsearch.exception.ResourceNotFoundException;
import kg.attractor.jobsearch.model.ContactInfo;
import kg.attractor.jobsearch.model.EducationInfo;
import kg.attractor.jobsearch.model.Resume;
import kg.attractor.jobsearch.model.WorkExperienceInfo;
import kg.attractor.jobsearch.service.ContactInfoService;
import kg.attractor.jobsearch.service.EducationInfoService;
import kg.attractor.jobsearch.service.ResumeService;
import kg.attractor.jobsearch.service.WorkExperienceInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeDao resumeDao;
    private final EducationInfoService educationInfoService;
    private final WorkExperienceInfoService workExperienceInfoService;
    private final ContactInfoService contactInfoService;

    @Override
    public Resume createResume(Resume resume, List<EducationInfo> educationList,
                               List<WorkExperienceInfo> workExperienceList, List<ContactInfo> contactList) {
        if (resume.getIsActive() == null) {
            resume.setIsActive(true);
        }

        Resume saved = resumeDao.save(resume);

        saveEducation(saved.getId(), educationList);
        saveWorkExperience(saved.getId(), workExperienceList);
        saveContacts(saved.getId(), contactList);

        return saved;
    }

    @Override
    public Resume updateResume(Long id, Resume resume, List<EducationInfo> educationList,
                               List<WorkExperienceInfo> workExperienceList, List<ContactInfo> contactList) {
        Resume existing = getResumeById(id);
        resume.setId(existing.getId());
        resume.setApplicantId(existing.getApplicantId());
        resumeDao.update(resume);

        educationInfoService.deleteByResumeId(id);
        saveEducation(id, educationList);

        workExperienceInfoService.deleteByResumeId(id);
        saveWorkExperience(id, workExperienceList);

        contactInfoService.deleteByResumeId(id);
        saveContacts(id, contactList);

        return resume;
    }

    @Override
    public void deleteResume(Long id) {
        getResumeById(id);
        educationInfoService.deleteByResumeId(id);
        workExperienceInfoService.deleteByResumeId(id);
        contactInfoService.deleteByResumeId(id);
        resumeDao.delete(id);
    }

    @Override
    public Resume getResumeById(Long id) {
        return resumeDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Резюме с id " + id + " не найдено"));
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
        return educationInfoService.findByResumeId(resumeId);
    }

    @Override
    public List<WorkExperienceInfo> getWorkExperienceByResumeId(Long resumeId) {
        return workExperienceInfoService.findByResumeId(resumeId);
    }

    @Override
    public List<ContactInfo> getContactsByResumeId(Long resumeId) {
        return contactInfoService.findByResumeId(resumeId);
    }

    private void saveEducation(Long resumeId, List<EducationInfo> educationList) {
        if (educationList == null) {
            return;
        }
        for (EducationInfo education : educationList) {
            education.setResumeId(resumeId);
            educationInfoService.save(education);
        }
    }

    private void saveWorkExperience(Long resumeId, List<WorkExperienceInfo> workExperienceList) {
        if (workExperienceList == null) {
            return;
        }
        for (WorkExperienceInfo workExperience : workExperienceList) {
            workExperience.setResumeId(resumeId);
            workExperienceInfoService.save(workExperience);
        }
    }

    private void saveContacts(Long resumeId, List<ContactInfo> contactList) {
        if (contactList == null) {
            return;
        }
        for (ContactInfo contact : contactList) {
            contact.setResumeId(resumeId);
            contactInfoService.save(contact);
        }
    }
}