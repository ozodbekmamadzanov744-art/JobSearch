package kg.attractor.jobsearch.controller;

import kg.attractor.jobsearch.model.RespondedApplicant;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.model.Vacancy;
import kg.attractor.jobsearch.service.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vacancies")
@RequiredArgsConstructor
public class VacancyController {

    private final VacancyService vacancyService;

    @PostMapping
    public ResponseEntity<Vacancy> createVacancy(@RequestBody Vacancy vacancy) {
        return ResponseEntity.ok(vacancyService.createVacancy(vacancy));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vacancy> updateVacancy(@PathVariable Long id,
                                                 @RequestBody Vacancy vacancy) {
        return ResponseEntity.ok(vacancyService.updateVacancy(id, vacancy));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Long id) {
        vacancyService.deleteVacancy(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Vacancy>> getAllActiveVacancies() {
        return ResponseEntity.ok(vacancyService.getAllActiveVacancies());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Vacancy>> getVacanciesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(vacancyService.getVacanciesByCategory(categoryId));
    }

    @PostMapping("/{id}/respond")
    public ResponseEntity<RespondedApplicant> respondToVacancy(@PathVariable Long id,
                                                               @RequestBody RespondedApplicant response) {
        return ResponseEntity.ok(vacancyService.respondToVacancy(id, response));
    }

    @GetMapping("/{id}/applicants")
    public ResponseEntity<List<User>> getApplicantsForVacancy(@PathVariable Long id) {
        return ResponseEntity.ok(vacancyService.getApplicantsForVacancy(id));
    }
}