package kg.attractor.jobsearch.controller;

import kg.attractor.jobsearch.model.RespondedApplicant;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.model.Vacancy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vacancies")
public class VacancyController {

    @PostMapping
    public ResponseEntity<Vacancy> createVacancy(@RequestBody Vacancy vacancy) {
        return ResponseEntity.ok(vacancy);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vacancy> updateVacancy(@PathVariable Long id,
                                                 @RequestBody Vacancy vacancy) {
        return ResponseEntity.ok(vacancy);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Vacancy>> getAllActiveVacancies() {
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Vacancy>> getVacanciesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/{id}/respond")
    public ResponseEntity<RespondedApplicant> respondToVacancy(@PathVariable Long id,
                                                               @RequestBody RespondedApplicant response) {
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/applicants")
    public ResponseEntity<List<User>> getApplicantsForVacancy(@PathVariable Long id) {
        return ResponseEntity.ok(List.of());
    }
}
