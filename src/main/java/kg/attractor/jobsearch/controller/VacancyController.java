package kg.attractor.jobsearch.controller;

import jakarta.validation.Valid;
import kg.attractor.jobsearch.dto.VacancyRequestDto;
import kg.attractor.jobsearch.dto.VacancyResponseDto;
import kg.attractor.jobsearch.mapper.VacancyMapper;
import kg.attractor.jobsearch.model.Vacancy;
import kg.attractor.jobsearch.security.CustomUserDetails;
import kg.attractor.jobsearch.service.VacancyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vacancies")
public class VacancyController {

    private final VacancyService vacancyService;

    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @PostMapping
    public ResponseEntity<VacancyResponseDto> createVacancy(@Valid @RequestBody VacancyRequestDto dto) {
        Vacancy vacancy = VacancyMapper.toModel(dto);
        return ResponseEntity.ok(VacancyMapper.toDto(vacancyService.createVacancy(vacancy)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VacancyResponseDto> updateVacancy(@PathVariable Long id, @Valid @RequestBody VacancyRequestDto dto,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Vacancy vacancy = VacancyMapper.toModel(dto);
        Vacancy updated = vacancyService.updateVacancy(id, vacancy, userDetails.getUser().getId());
        return ResponseEntity.ok(VacancyMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        vacancyService.deleteVacancy(id, userDetails.getUser().getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VacancyResponseDto> getVacancyById(@PathVariable Long id) {
        return ResponseEntity.ok(VacancyMapper.toDto(vacancyService.getVacancyById(id)));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<VacancyResponseDto>> getVacanciesByAuthor(@PathVariable Long authorId) {
        List<VacancyResponseDto> result = vacancyService.getVacanciesByAuthor(authorId).stream()
                .map(VacancyMapper::toDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<VacancyResponseDto>> getAllActiveVacancies() {
        List<VacancyResponseDto> result = vacancyService.getAllActiveVacancies().stream()
                .map(VacancyMapper::toDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<VacancyResponseDto>> getVacanciesByCategory(@PathVariable Long categoryId) {
        List<VacancyResponseDto> result = vacancyService.getVacanciesByCategory(categoryId).stream()
                .map(VacancyMapper::toDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/responded-by/{applicantId}")
    public ResponseEntity<List<VacancyResponseDto>> getVacanciesRespondedByApplicant(@PathVariable Long applicantId) {
        List<VacancyResponseDto> result = vacancyService.getVacanciesByApplicant(applicantId).stream()
                .map(VacancyMapper::toDto)
                .toList();
        return ResponseEntity.ok(result);
    }
}