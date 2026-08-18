package kg.attractor.jobsearch.service.impl;

import kg.attractor.jobsearch.exception.ForbiddenOperationException;
import kg.attractor.jobsearch.exception.ResourceNotFoundException;
import kg.attractor.jobsearch.model.ContactInfo;
import kg.attractor.jobsearch.model.EducationInfo;
import kg.attractor.jobsearch.model.Resume;
import kg.attractor.jobsearch.model.WorkExperienceInfo;
import kg.attractor.jobsearch.repository.ResumeRepository;
import kg.attractor.jobsearch.service.ContactInfoService;
import kg.attractor.jobsearch.service.EducationInfoService;
import kg.attractor.jobsearch.service.ResumeService;
import kg.attractor.jobsearch.service.WorkExperienceInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final EducationInfoService educationInfoService;
    private final WorkExperienceInfoService workExperienceInfoService;
    private final ContactInfoService contactInfoService;

    @Override
    public Resume createResume(Resume resume, List<EducationInfo> educationList,
                               List<WorkExperienceInfo> workExperienceList, List<ContactInfo> contactList) {
        log.info("Создание резюме '{}' для соискателя id={}", resume.getName(), resume.getApplicantId());
        if (resume.getIsActive() == null) {
            resume.setIsActive(true);
        }

        Resume saved = resumeRepository.save(resume);

        saveEducation(saved.getId(), educationList);
        saveWorkExperience(saved.getId(), workExperienceList);
        saveContacts(saved.getId(), contactList);

        log.info("Резюме id={} успешно создано", saved.getId());
        return saved;
    }

    @Override
    public Resume updateResume(Long id, Resume resume, List<EducationInfo> educationList,
                               List<WorkExperienceInfo> workExperienceList, List<ContactInfo> contactList, Long currentUserId) {
        Resume existing = getResumeById(id);

        if (!existing.getApplicantId().equals(currentUserId)) {
            throw new ForbiddenOperationException("Вы не являетесь владельцем этого резюме");
        }

        existing.setName(resume.getName());
        existing.setCategoryId(resume.getCategoryId());
        existing.setSalary(resume.getSalary());
        existing.setIsActive(resume.getIsActive());
        resumeRepository.save(existing);

        educationInfoService.deleteByResumeId(id);
        saveEducation(id, educationList);

        workExperienceInfoService.deleteByResumeId(id);
        saveWorkExperience(id, workExperienceList);

        contactInfoService.deleteByResumeId(id);
        saveContacts(id, contactList);

        return existing;
    }

    @Override
    public void deleteResume(Long id, Long currentUserId) {
        Resume existing = getResumeById(id);

        if (!existing.getApplicantId().equals(currentUserId)) {
            throw new ForbiddenOperationException("Вы не являетесь владельцем этого резюме");
        }

        educationInfoService.deleteByResumeId(id);
        workExperienceInfoService.deleteByResumeId(id);
        contactInfoService.deleteByResumeId(id);
        resumeRepository.deleteById(id);
    }

    @Override
    public Resume getResumeById(Long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Резюме с id " + id + " не найдено"));
    }

    @Override
    public List<Resume> getAllActiveResumes() {
        return resumeRepository.findByIsActiveTrue(Pageable.unpaged()).getContent();
    }

    @Override
    public List<Resume> getResumesByCategory(Long categoryId) {
        return resumeRepository.findByCategoryId(categoryId).stream()
                .filter(resume -> Boolean.TRUE.equals(resume.getIsActive()))
                .toList();
    }

    @Override
    public List<Resume> getResumesByApplicant(Long applicantId) {
        return resumeRepository.findByApplicantId(applicantId, Pageable.unpaged()).getContent();
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

    @Override
    public Page<Resume> getAllActiveResumes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updateTime"));
        return resumeRepository.findByIsActiveTrue(pageable);
    }

    @Override
    public Page<Resume> getResumesByApplicant(Long applicantId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updateTime"));
        return resumeRepository.findByApplicantId(applicantId, pageable);
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
