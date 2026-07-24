package kg.attractor.jobsearch.mapper;

import kg.attractor.jobsearch.dto.ResumeRequestDto;
import kg.attractor.jobsearch.dto.ResumeResponseDto;
import kg.attractor.jobsearch.model.Resume;

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

    public static ResumeResponseDto toDto(Resume resume) {
        ResumeResponseDto dto = new ResumeResponseDto();
        dto.setId(resume.getId());
        dto.setApplicantId(resume.getApplicantId());
        dto.setName(resume.getName());
        dto.setCategoryId(resume.getCategoryId());
        dto.setSalary(resume.getSalary());
        dto.setIsActive(resume.getIsActive());
        dto.setCreatedDate(resume.getCreatedDate());
        dto.setUpdateTime(resume.getUpdateTime());
        return dto;
    }
}