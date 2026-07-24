package kg.attractor.jobsearch.mapper;

import kg.attractor.jobsearch.dto.RespondedApplicantRequestDto;
import kg.attractor.jobsearch.dto.RespondedApplicantResponseDto;
import kg.attractor.jobsearch.model.RespondedApplicant;

public class RespondedApplicantMapper {

    private RespondedApplicantMapper() {
    }

    public static RespondedApplicant toModel(RespondedApplicantRequestDto dto) {
        RespondedApplicant respondedApplicant = new RespondedApplicant();
        respondedApplicant.setResumeId(dto.getResumeId());
        return respondedApplicant;
    }

    public static RespondedApplicantResponseDto toDto(RespondedApplicant respondedApplicant) {
        RespondedApplicantResponseDto dto = new RespondedApplicantResponseDto();
        dto.setId(respondedApplicant.getId());
        dto.setResumeId(respondedApplicant.getResumeId());
        dto.setVacancyId(respondedApplicant.getVacancyId());
        dto.setConfirmation(respondedApplicant.getConfirmation());
        return dto;
    }
}