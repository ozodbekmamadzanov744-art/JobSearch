package kg.attractor.jobsearch.mapper;

import kg.attractor.jobsearch.dto.RespondedApplicantRequestDto;
import kg.attractor.jobsearch.dto.RespondedApplicantResponseDto;
import kg.attractor.jobsearch.model.RespondedApplicant;
import kg.attractor.jobsearch.model.Resume;

public class RespondedApplicantMapper {

    private RespondedApplicantMapper() {
    }

    public static RespondedApplicant toModel(RespondedApplicantRequestDto dto) {
        RespondedApplicant respondedApplicant = new RespondedApplicant();

        if (dto.getResumeId() != null) {
            Resume resume = new Resume();
            resume.setId(dto.getResumeId());
            respondedApplicant.setResume(resume);
        }

        return respondedApplicant;
    }

    public static RespondedApplicantResponseDto toDto(RespondedApplicant respondedApplicant) {
        RespondedApplicantResponseDto dto = new RespondedApplicantResponseDto();
        dto.setId(respondedApplicant.getId());
        dto.setResumeId(respondedApplicant.getResume() != null ? respondedApplicant.getResume().getId() : null);
        dto.setVacancyId(respondedApplicant.getVacancy() != null ? respondedApplicant.getVacancy().getId() : null);
        dto.setConfirmation(respondedApplicant.getConfirmation());
        return dto;
    }
}