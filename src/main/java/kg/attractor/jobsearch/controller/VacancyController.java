package kg.attractor.jobsearch.controller;

import kg.attractor.jobsearch.model.ModelRespondedApplicant;
import kg.attractor.jobsearch.model.ModelUser;
import kg.attractor.jobsearch.model.ModelVacancy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vacancies")
public class VacancyController {

    @PostMapping
    public ResponseEntity<ModelVacancy> createVacancy(@RequestBody ModelVacancy vacancy) {
        return ResponseEntity.ok(vacancy);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModelVacancy> updateVacancy(@PathVariable Long id,
                                                      @RequestBody ModelVacancy vacancy) {
        return ResponseEntity.ok(vacancy);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ModelVacancy>> getAllActiveVacancies() {
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ModelVacancy>> getVacanciesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/{id}/respond")
    public ResponseEntity<ModelRespondedApplicant> respondToVacancy(@PathVariable Long id,
                                                                    @RequestBody ModelRespondedApplicant response) {
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/applicants")
    public ResponseEntity<List<ModelUser>> getApplicantsForVacancy(@PathVariable Long id) {
        return ResponseEntity.ok(List.of());
    }
}
