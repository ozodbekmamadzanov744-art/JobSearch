package kg.attractor.jobsearch.controller;

import jakarta.validation.Valid;
import kg.attractor.jobsearch.dto.ContactInfoDto;
import kg.attractor.jobsearch.dto.EducationInfoDto;
import kg.attractor.jobsearch.dto.ResumeFormDto;
import kg.attractor.jobsearch.dto.WorkExperienceInfoDto;
import kg.attractor.jobsearch.exception.ForbiddenOperationException;
import kg.attractor.jobsearch.mapper.ResumeMapper;
import kg.attractor.jobsearch.model.ContactInfo;
import kg.attractor.jobsearch.model.EducationInfo;
import kg.attractor.jobsearch.model.Resume;
import kg.attractor.jobsearch.model.WorkExperienceInfo;
import kg.attractor.jobsearch.security.CustomUserDetails;
import kg.attractor.jobsearch.service.CategoryService;
import kg.attractor.jobsearch.service.ContactTypeService;
import kg.attractor.jobsearch.service.ResumeService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Controller
@RequestMapping("/pages/resumes")
public class ResumePageController {

    private static final int EMPTY_ROWS = 3;

    private final ResumeService resumeService;
    private final CategoryService categoryService;
    private final ContactTypeService contactTypeService;

    public ResumePageController(ResumeService resumeService, CategoryService categoryService,
                                ContactTypeService contactTypeService) {
        this.resumeService = resumeService;
        this.categoryService = categoryService;
        this.contactTypeService = contactTypeService;
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        ResumeFormDto dto = new ResumeFormDto();
        padTo3(dto.getEducationList(), EducationInfoDto::new);
        padTo3(dto.getWorkExperienceList(), WorkExperienceInfoDto::new);
        padTo3(dto.getContactList(), ContactInfoDto::new);

        model.addAttribute("resumeDto", dto);
        addReferenceData(model);
        return "resumes/form";
    }

    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("resumeDto") ResumeFormDto dto,
                         BindingResult bindingResult,
                         @AuthenticationPrincipal CustomUserDetails userDetails,
                         Model model) {
        if (bindingResult.hasErrors()) {
            addReferenceData(model);
            return "resumes/form";
        }

        Resume resume = toModel(dto);
        resume.setApplicantId(userDetails.getUser().getId());

        resumeService.createResume(resume, filterEducation(dto), filterExperience(dto), filterContacts(dto));

        return "redirect:/pages/cabinet";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Resume resume = resumeService.getResumeById(id);
        if (!resume.getApplicantId().equals(userDetails.getUser().getId())) {
            throw new ForbiddenOperationException("Вы не являетесь владельцем этого резюме");
        }

        ResumeFormDto dto = new ResumeFormDto();
        dto.setName(resume.getName());
        dto.setCategoryId(resume.getCategoryId());
        dto.setSalary(resume.getSalary());
        dto.setIsActive(resume.getIsActive());

        List<EducationInfoDto> education = new ArrayList<>(
                resumeService.getEducationByResumeId(id).stream().map(ResumeMapper::toDto).toList());
        List<WorkExperienceInfoDto> experience = new ArrayList<>(
                resumeService.getWorkExperienceByResumeId(id).stream().map(ResumeMapper::toDto).toList());
        List<ContactInfoDto> contacts = new ArrayList<>(
                resumeService.getContactsByResumeId(id).stream().map(ResumeMapper::toDto).toList());

        padTo3(education, EducationInfoDto::new);
        padTo3(experience, WorkExperienceInfoDto::new);
        padTo3(contacts, ContactInfoDto::new);

        dto.setEducationList(education);
        dto.setWorkExperienceList(experience);
        dto.setContactList(contacts);

        model.addAttribute("resumeDto", dto);
        model.addAttribute("resumeId", id);
        addReferenceData(model);
        return "resumes/form";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id,
                       @Valid @ModelAttribute("resumeDto") ResumeFormDto dto,
                       BindingResult bindingResult,
                       @AuthenticationPrincipal CustomUserDetails userDetails,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("resumeId", id);
            addReferenceData(model);
            return "resumes/form";
        }

        Resume resume = toModel(dto);
        resumeService.updateResume(id, resume, filterEducation(dto), filterExperience(dto), filterContacts(dto),
                userDetails.getUser().getId());

        return "redirect:/pages/cabinet";
    }

    private void addReferenceData(Model model) {
        Map<String, String> categories = new LinkedHashMap<>();
        categoryService.getAllCategories().forEach(c -> categories.put(String.valueOf(c.getId()), c.getName()));
        model.addAttribute("categories", categories);

        Map<String, String> contactTypes = new LinkedHashMap<>();
        contactTypeService.getAllContactTypes().forEach(t -> contactTypes.put(String.valueOf(t.getId()), t.getType()));
        model.addAttribute("contactTypes", contactTypes);
    }

    private Resume toModel(ResumeFormDto dto) {
        Resume resume = new Resume();
        resume.setName(dto.getName());
        resume.setCategoryId(dto.getCategoryId());
        resume.setSalary(dto.getSalary());
        resume.setIsActive(dto.getIsActive() == null || dto.getIsActive());
        return resume;
    }

    private List<EducationInfo> filterEducation(ResumeFormDto dto) {
        return dto.getEducationList().stream()
                .filter(e -> notBlank(e.getInstitution()) && notBlank(e.getProgram())
                        && notBlank(e.getDegree()) && e.getStartDate() != null)
                .map(ResumeMapper::toModel)
                .toList();
    }

    private List<WorkExperienceInfo> filterExperience(ResumeFormDto dto) {
        return dto.getWorkExperienceList().stream()
                .filter(w -> notBlank(w.getCompanyName()) && notBlank(w.getPosition())
                        && notBlank(w.getResponsibilities()) && w.getYears() != null)
                .map(ResumeMapper::toModel)
                .toList();
    }

    private List<ContactInfo> filterContacts(ResumeFormDto dto) {
        return dto.getContactList().stream()
                .filter(c -> c.getTypeId() != null && notBlank(c.getValue()))
                .map(ResumeMapper::toModel)
                .toList();
    }

    private boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    private <T> void padTo3(List<T> list, Supplier<T> supplier) {
        while (list.size() < EMPTY_ROWS) {
            list.add(supplier.get());
        }
    }
}
