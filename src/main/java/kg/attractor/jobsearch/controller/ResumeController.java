package kg.attractor.jobsearch.controller;

import kg.attractor.jobsearch.model.ModelResume;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resumes")
public class ResumeController {

    @PostMapping
    public ResponseEntity<ModelResume> createResume(@RequestBody ModelResume resume) {
        return ResponseEntity.ok(resume);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModelResume> updateResume(@PathVariable Long id,
                                                    @RequestBody ModelResume resume) {
        return ResponseEntity.ok(resume);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ModelResume>> getAllResumes() {
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ModelResume>> getResumesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(List.of());
    }
}