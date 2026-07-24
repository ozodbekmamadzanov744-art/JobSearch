package kg.attractor.jobsearch.controller;

import jakarta.validation.Valid;
import kg.attractor.jobsearch.dto.RespondedApplicantRequestDto;
import kg.attractor.jobsearch.dto.RespondedApplicantResponseDto;
import kg.attractor.jobsearch.dto.UserResponseDto;
import kg.attractor.jobsearch.mapper.RespondedApplicantMapper;
import kg.attractor.jobsearch.mapper.UserMapper;
import kg.attractor.jobsearch.model.RespondedApplicant;
import kg.attractor.jobsearch.service.VacancyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vacancies")
public class RespondedApplicantController {

    private final VacancyService vacancyService;

    public RespondedApplicantController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @PostMapping("/{id}/respond")
    public ResponseEntity<RespondedApplicantResponseDto> respondToVacancy(@PathVariable Long id, @Valid
                                                                          @RequestBody RespondedApplicantRequestDto dto) {
        RespondedApplicant respondedApplicant = RespondedApplicantMapper.toModel(dto);
        RespondedApplicant saved = vacancyService.respondToVacancy(id, respondedApplicant);
        return ResponseEntity.ok(RespondedApplicantMapper.toDto(saved));
    }

    @GetMapping("/{id}/applicants")
    public ResponseEntity<List<UserResponseDto>> getApplicantsForVacancy(@PathVariable Long id) {
        List<UserResponseDto> result = vacancyService.getApplicantsForVacancy(id).stream()
                .map(UserMapper::toDto)
                .toList();
        return ResponseEntity.ok(result);
    }
}