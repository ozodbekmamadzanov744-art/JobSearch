package kg.attractor.jobsearch.service;

import kg.attractor.jobsearch.model.ContactInfo;
import kg.attractor.jobsearch.model.EducationInfo;
import kg.attractor.jobsearch.model.Resume;
import kg.attractor.jobsearch.model.WorkExperienceInfo;

import java.util.List;

public interface ResumeService {

    Resume createResume(Resume resume, List<EducationInfo> educationList,
                        List<WorkExperienceInfo> workExperienceList, List<ContactInfo> contactList);

    Resume updateResume(Long id, Resume resume, List<EducationInfo> educationList,
                        List<WorkExperienceInfo> workExperienceList, List<ContactInfo> contactList);

    void deleteResume(Long id);

    Resume getResumeById(Long id);

    List<Resume> getAllActiveResumes();

    List<Resume> getResumesByCategory(Long categoryId);

    List<Resume> getResumesByApplicant(Long applicantId);

    List<EducationInfo> getEducationByResumeId(Long resumeId);

    List<WorkExperienceInfo> getWorkExperienceByResumeId(Long resumeId);

    List<ContactInfo> getContactsByResumeId(Long resumeId);
}