package kg.attractor.jobsearch.controller;

import jakarta.validation.Valid;
import kg.attractor.jobsearch.dto.ResumeRequestDto;
import kg.attractor.jobsearch.dto.ResumeResponseDto;
import kg.attractor.jobsearch.mapper.ResumeMapper;
import kg.attractor.jobsearch.model.EducationInfo;
import kg.attractor.jobsearch.model.Resume;
import kg.attractor.jobsearch.model.WorkExperienceInfo;
import kg.attractor.jobsearch.service.ResumeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping
    public ResponseEntity<ResumeResponseDto> createResume(@Valid @RequestBody ResumeRequestDto dto) {
        Resume resume = ResumeMapper.toModel(dto);
        List<EducationInfo> educationList = mapEducation(dto);
        List<WorkExperienceInfo> workExperienceList = mapWorkExperience(dto);

        Resume saved = resumeService.createResume(resume, educationList, workExperienceList);
        return ResponseEntity.ok(toFullDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResumeResponseDto> updateResume(@PathVariable Long id, @Valid @RequestBody ResumeRequestDto dto) {
        Resume resume = ResumeMapper.toModel(dto);
        List<EducationInfo> educationList = mapEducation(dto);
        List<WorkExperienceInfo> workExperienceList = mapWorkExperience(dto);

        Resume updated = resumeService.updateResume(id, resume, educationList, workExperienceList);
        return ResponseEntity.ok(toFullDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) {
        resumeService.deleteResume(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeResponseDto> getResumeById(@PathVariable Long id) {
        return ResponseEntity.ok(toFullDto(resumeService.getResumeById(id)));
    }

    @GetMapping
    public ResponseEntity<List<ResumeResponseDto>> getAllResumes() {
        List<ResumeResponseDto> result = resumeService.getAllActiveResumes().stream()
                .map(this::toFullDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ResumeResponseDto>> getResumesByCategory(@PathVariable Long categoryId) {
        List<ResumeResponseDto> result = resumeService.getResumesByCategory(categoryId).stream()
                .map(this::toFullDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/applicant/{applicantId}")
    public ResponseEntity<List<ResumeResponseDto>> getResumesByApplicant(@PathVariable Long applicantId) {
        List<ResumeResponseDto> result = resumeService.getResumesByApplicant(applicantId).stream()
                .map(this::toFullDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    private List<EducationInfo> mapEducation(ResumeRequestDto dto) {
        if (dto.getEducationList() == null) {
            return List.of();
        }
        return dto.getEducationList().stream()
                .filter(Objects::nonNull)
                .map(ResumeMapper::toModel)
                .toList();
    }

    private List<WorkExperienceInfo> mapWorkExperience(ResumeRequestDto dto) {
        if (dto.getWorkExperienceList() == null) {
            return List.of();
        }
        return dto.getWorkExperienceList().stream()
                .filter(Objects::nonNull)
                .map(ResumeMapper::toModel)
                .toList();
    }

    private ResumeResponseDto toFullDto(Resume resume) {
        List<EducationInfo> educationList = resumeService.getEducationByResumeId(resume.getId());
        List<WorkExperienceInfo> workExperienceList = resumeService.getWorkExperienceByResumeId(resume.getId());
        return ResumeMapper.toDto(resume, educationList, workExperienceList);
    }
}