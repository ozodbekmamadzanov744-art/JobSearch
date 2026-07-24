package kg.attractor.jobsearch.controller;

import kg.attractor.jobsearch.dto.ResumeRequestDto;
import kg.attractor.jobsearch.dto.ResumeResponseDto;
import kg.attractor.jobsearch.mapper.ResumeMapper;
import kg.attractor.jobsearch.model.Resume;
import kg.attractor.jobsearch.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping
    public ResponseEntity<ResumeResponseDto> createResume(@RequestBody ResumeRequestDto dto) {
        Resume resume = ResumeMapper.toModel(dto);
        return ResponseEntity.ok(ResumeMapper.toDto(resumeService.createResume(resume)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResumeResponseDto> updateResume(@PathVariable Long id,
                                                          @RequestBody ResumeRequestDto dto) {
        Resume resume = ResumeMapper.toModel(dto);
        return ResponseEntity.ok(ResumeMapper.toDto(resumeService.updateResume(id, resume)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) {
        resumeService.deleteResume(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ResumeResponseDto>> getAllResumes() {
        List<ResumeResponseDto> result = resumeService.getAllActiveResumes().stream()
                .map(ResumeMapper::toDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ResumeResponseDto>> getResumesByCategory(@PathVariable Long categoryId) {
        List<ResumeResponseDto> result = resumeService.getResumesByCategory(categoryId).stream()
                .map(ResumeMapper::toDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/applicant/{applicantId}")
    public ResponseEntity<List<ResumeResponseDto>> getResumesByApplicant(@PathVariable Long applicantId) {
        List<ResumeResponseDto> result = resumeService.getResumesByApplicant(applicantId).stream()
                .map(ResumeMapper::toDto)
                .toList();
        return ResponseEntity.ok(result);
    }
}