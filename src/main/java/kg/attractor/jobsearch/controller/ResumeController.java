package kg.attractor.jobsearch.controller;

import kg.attractor.jobsearch.model.Resume;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resumes")
public class ResumeController {

    @PostMapping
    public ResponseEntity<Resume> createResume(@RequestBody Resume resume) {
        return ResponseEntity.ok(resume);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resume> updateResume(@PathVariable Long id,
                                               @RequestBody Resume resume) {
        return ResponseEntity.ok(resume);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Resume>> getAllResumes() {
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Resume>> getResumesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(List.of());
    }
}