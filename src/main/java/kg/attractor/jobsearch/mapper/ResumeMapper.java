package kg.attractor.jobsearch.mapper;

import kg.attractor.jobsearch.dto.*;
import kg.attractor.jobsearch.model.ContactInfo;
import kg.attractor.jobsearch.model.EducationInfo;
import kg.attractor.jobsearch.model.Resume;
import kg.attractor.jobsearch.model.WorkExperienceInfo;

import java.util.List;

public class ResumeMapper {

    private ResumeMapper() {
    }

    public static Resume toModel(ResumeRequestDto dto) {
        Resume resume = new Resume();
        resume.setApplicantId(dto.getApplicantId());
        resume.setName(dto.getName());
        resume.setCategoryId(dto.getCategoryId());
        resume.setSalary(dto.getSalary());
        resume.setIsActive(dto.getIsActive());
        return resume;
    }

    public static ResumeResponseDto toDto(Resume resume, List<EducationInfo> educationList,
                                          List<WorkExperienceInfo> workExperienceList, List<ContactInfo> contactList) {
        ResumeResponseDto dto = new ResumeResponseDto();
        dto.setId(resume.getId());
        dto.setApplicantId(resume.getApplicantId());
        dto.setName(resume.getName());
        dto.setCategoryId(resume.getCategoryId());
        dto.setSalary(resume.getSalary());
        dto.setIsActive(resume.getIsActive());
        dto.setCreatedDate(resume.getCreatedDate());
        dto.setUpdateTime(resume.getUpdateTime());
        dto.setEducationList(educationList.stream().map(ResumeMapper::toDto).toList());
        dto.setWorkExperienceList(workExperienceList.stream().map(ResumeMapper::toDto).toList());
        dto.setContactList(contactList.stream().map(ResumeMapper::toDto).toList());
        return dto;
    }

    public static ContactInfo toModel(ContactInfoDto dto) {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setTypeId(dto.getTypeId());
        contactInfo.setValue(dto.getValue());
        return contactInfo;
    }

    public static ContactInfoDto toDto(ContactInfo contactInfo) {
        ContactInfoDto dto = new ContactInfoDto();
        dto.setId(contactInfo.getId());
        dto.setTypeId(contactInfo.getTypeId());
        dto.setValue(contactInfo.getValue());
        return dto;
    }

    public static EducationInfo toModel(EducationInfoDto dto) {
        EducationInfo educationInfo = new EducationInfo();
        educationInfo.setInstitution(dto.getInstitution());
        educationInfo.setProgram(dto.getProgram());
        educationInfo.setStartDate(dto.getStartDate());
        educationInfo.setEndDate(dto.getEndDate());
        educationInfo.setDegree(dto.getDegree());
        return educationInfo;
    }

    public static EducationInfoDto toDto(EducationInfo educationInfo) {
        EducationInfoDto dto = new EducationInfoDto();
        dto.setId(educationInfo.getId());
        dto.setInstitution(educationInfo.getInstitution());
        dto.setProgram(educationInfo.getProgram());
        dto.setStartDate(educationInfo.getStartDate());
        dto.setEndDate(educationInfo.getEndDate());
        dto.setDegree(educationInfo.getDegree());
        return dto;
    }

    public static WorkExperienceInfo toModel(WorkExperienceInfoDto dto) {
        WorkExperienceInfo workExperienceInfo = new WorkExperienceInfo();
        workExperienceInfo.setYears(dto.getYears());
        workExperienceInfo.setCompanyName(dto.getCompanyName());
        workExperienceInfo.setPosition(dto.getPosition());
        workExperienceInfo.setResponsibilities(dto.getResponsibilities());
        return workExperienceInfo;
    }

    public static WorkExperienceInfoDto toDto(WorkExperienceInfo workExperienceInfo) {
        WorkExperienceInfoDto dto = new WorkExperienceInfoDto();
        dto.setId(workExperienceInfo.getId());
        dto.setYears(workExperienceInfo.getYears());
        dto.setCompanyName(workExperienceInfo.getCompanyName());
        dto.setPosition(workExperienceInfo.getPosition());
        dto.setResponsibilities(workExperienceInfo.getResponsibilities());
        return dto;
    }
}