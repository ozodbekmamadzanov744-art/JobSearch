package kg.attractor.jobsearch.controller;

import jakarta.validation.Valid;
import kg.attractor.jobsearch.dto.ContactInfoDto;
import kg.attractor.jobsearch.dto.EducationInfoDto;
import kg.attractor.jobsearch.dto.ResumeFormDto;
import kg.attractor.jobsearch.dto.WorkExperienceInfoDto;
import kg.attractor.jobsearch.exception.ForbiddenOperationException;
import kg.attractor.jobsearch.mapper.ResumeMapper;
import kg.attractor.jobsearch.model.Category;
import kg.attractor.jobsearch.model.ContactInfo;
import kg.attractor.jobsearch.model.EducationInfo;
import kg.attractor.jobsearch.model.Resume;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.model.WorkExperienceInfo;
import kg.attractor.jobsearch.security.CustomUserDetails;
import kg.attractor.jobsearch.service.CategoryService;
import kg.attractor.jobsearch.service.ContactTypeService;
import kg.attractor.jobsearch.service.ResumeService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    public ResumePageController(ResumeService resumeService,
                                CategoryService categoryService,
                                ContactTypeService contactTypeService) {
        this.resumeService = resumeService;
        this.categoryService = categoryService;
        this.contactTypeService = contactTypeService;
    }

    private static final int DEFAULT_PAGE_SIZE = 5;

    @GetMapping
    public String resumes(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Resume> resumePage = resumeService.getAllActiveResumes(page, DEFAULT_PAGE_SIZE);
        model.addAttribute("resumePage", resumePage);
        model.addAttribute("resumes", resumePage.getContent());
        return "resumes/list";
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

        validateAdditionalBlocks(dto, bindingResult);

        if (bindingResult.hasErrors()) {
            addReferenceData(model);
            return "resumes/form";
        }

        Resume resume = toModel(dto);
        User applicant = new User();
        applicant.setId(userDetails.getUser().getId());
        resume.setApplicant(applicant);

        resumeService.createResume(
                resume,
                filterEducation(dto),
                filterExperience(dto),
                filterContacts(dto)
        );

        return "redirect:/pages/cabinet";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                           Model model,
                           @AuthenticationPrincipal CustomUserDetails userDetails) {

        Resume resume = resumeService.getResumeById(id);

        if (resume.getApplicant() == null || !resume.getApplicant().getId().equals(userDetails.getUser().getId())) {
            throw new ForbiddenOperationException(
                    "Вы не являетесь владельцем этого резюме"
            );
        }

        ResumeFormDto dto = new ResumeFormDto();

        dto.setName(resume.getName());
        dto.setCategoryId(resume.getCategory() != null ? resume.getCategory().getId() : null);
        dto.setSalary(resume.getSalary());
        dto.setIsActive(resume.getIsActive());

        List<EducationInfoDto> education = new ArrayList<>(
                resumeService.getEducationByResumeId(id)
                        .stream()
                        .map(ResumeMapper::toDto)
                        .toList()
        );

        List<WorkExperienceInfoDto> experience = new ArrayList<>(
                resumeService.getWorkExperienceByResumeId(id)
                        .stream()
                        .map(ResumeMapper::toDto)
                        .toList()
        );

        List<ContactInfoDto> contacts = new ArrayList<>(
                resumeService.getContactsByResumeId(id)
                        .stream()
                        .map(ResumeMapper::toDto)
                        .toList()
        );

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

        validateAdditionalBlocks(dto, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("resumeId", id);
            addReferenceData(model);
            return "resumes/form";
        }

        Resume resume = toModel(dto);

        resumeService.updateResume(
                id,
                resume,
                filterEducation(dto),
                filterExperience(dto),
                filterContacts(dto),
                userDetails.getUser().getId()
        );

        return "redirect:/pages/cabinet";
    }

    private void validateAdditionalBlocks(ResumeFormDto dto,
                                          BindingResult bindingResult) {

        validateEducation(dto.getEducationList(), bindingResult);
        validateWorkExperience(dto.getWorkExperienceList(), bindingResult);
        validateContacts(dto.getContactList(), bindingResult);
    }

    private void validateEducation(List<EducationInfoDto> educationList,
                                   BindingResult bindingResult) {

        if (educationList == null) {
            return;
        }

        for (int i = 0; i < educationList.size(); i++) {

            EducationInfoDto education = educationList.get(i);

            if (isEducationEmpty(education)) {
                continue;
            }

            if (!notBlank(education.getInstitution())) {
                bindingResult.rejectValue(
                        "educationList[" + i + "].institution",
                        "education.institution",
                        "Учебное заведение обязательно для заполнения"
                );
            }

            if (!notBlank(education.getProgram())) {
                bindingResult.rejectValue(
                        "educationList[" + i + "].program",
                        "education.program",
                        "Программа обучения обязательна для заполнения"
                );
            }

            if (education.getStartDate() == null) {
                bindingResult.rejectValue(
                        "educationList[" + i + "].startDate",
                        "education.startDate",
                        "Дата начала обучения обязательна"
                );
            }

            if (!notBlank(education.getDegree())) {
                bindingResult.rejectValue(
                        "educationList[" + i + "].degree",
                        "education.degree",
                        "Степень обязательна для заполнения"
                );
            }
        }
    }

    private void validateWorkExperience(
            List<WorkExperienceInfoDto> experienceList,
            BindingResult bindingResult) {

        if (experienceList == null) {
            return;
        }

        for (int i = 0; i < experienceList.size(); i++) {

            WorkExperienceInfoDto experience = experienceList.get(i);

            if (isWorkExperienceEmpty(experience)) {
                continue;
            }

            if (experience.getYears() == null) {
                bindingResult.rejectValue(
                        "workExperienceList[" + i + "].years",
                        "experience.years",
                        "Количество лет опыта обязательно"
                );
            } else if (experience.getYears() < 0) {
                bindingResult.rejectValue(
                        "workExperienceList[" + i + "].years",
                        "experience.years",
                        "Количество лет опыта не может быть отрицательным"
                );
            }

            if (!notBlank(experience.getCompanyName())) {
                bindingResult.rejectValue(
                        "workExperienceList[" + i + "].companyName",
                        "experience.companyName",
                        "Название компании обязательно для заполнения"
                );
            }

            if (!notBlank(experience.getPosition())) {
                bindingResult.rejectValue(
                        "workExperienceList[" + i + "].position",
                        "experience.position",
                        "Должность обязательна для заполнения"
                );
            }

            if (!notBlank(experience.getResponsibilities())) {
                bindingResult.rejectValue(
                        "workExperienceList[" + i + "].responsibilities",
                        "experience.responsibilities",
                        "Обязанности обязательны для заполнения"
                );
            }
        }
    }

    private void validateContacts(List<ContactInfoDto> contactList,
                                  BindingResult bindingResult) {

        if (contactList == null) {
            return;
        }

        for (int i = 0; i < contactList.size(); i++) {

            ContactInfoDto contact = contactList.get(i);

            if (isContactEmpty(contact)) {
                continue;
            }

            if (contact.getTypeId() == null) {
                bindingResult.rejectValue(
                        "contactList[" + i + "].typeId",
                        "contact.typeId",
                        "Тип контакта обязателен"
                );
            }

            if (!notBlank(contact.getValue())) {
                bindingResult.rejectValue(
                        "contactList[" + i + "].value",
                        "contact.value",
                        "Значение контакта обязательно для заполнения"
                );
            }
        }
    }

    private boolean isEducationEmpty(EducationInfoDto education) {
        return education == null
                || (!notBlank(education.getInstitution())
                && !notBlank(education.getProgram())
                && education.getStartDate() == null
                && education.getEndDate() == null
                && !notBlank(education.getDegree()));
    }

    private boolean isWorkExperienceEmpty(WorkExperienceInfoDto experience) {
        return experience == null
                || (experience.getYears() == null
                && !notBlank(experience.getCompanyName())
                && !notBlank(experience.getPosition())
                && !notBlank(experience.getResponsibilities()));
    }

    private boolean isContactEmpty(ContactInfoDto contact) {
        return contact == null
                || (contact.getTypeId() == null
                && !notBlank(contact.getValue()));
    }

    private void addReferenceData(Model model) {

        Map<String, String> categories = new LinkedHashMap<>();

        categoryService.getAllCategories()
                .forEach(c ->
                        categories.put(
                                String.valueOf(c.getId()),
                                c.getName()
                        )
                );

        model.addAttribute("categories", categories);

        Map<String, String> contactTypes = new LinkedHashMap<>();

        contactTypeService.getAllContactTypes()
                .forEach(t ->
                        contactTypes.put(
                                String.valueOf(t.getId()),
                                t.getType()
                        )
                );

        model.addAttribute("contactTypes", contactTypes);
    }

    private Resume toModel(ResumeFormDto dto) {

        Resume resume = new Resume();

        resume.setName(dto.getName());

        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            resume.setCategory(category);
        }

        resume.setSalary(dto.getSalary());
        resume.setIsActive(
                dto.getIsActive() == null || dto.getIsActive()
        );

        return resume;
    }

    private List<EducationInfo> filterEducation(ResumeFormDto dto) {

        return dto.getEducationList()
                .stream()
                .filter(e ->
                        notBlank(e.getInstitution())
                                && notBlank(e.getProgram())
                                && notBlank(e.getDegree())
                                && e.getStartDate() != null
                )
                .map(ResumeMapper::toModel)
                .toList();
    }

    private List<WorkExperienceInfo> filterExperience(ResumeFormDto dto) {

        return dto.getWorkExperienceList()
                .stream()
                .filter(w ->
                        notBlank(w.getCompanyName())
                                && notBlank(w.getPosition())
                                && notBlank(w.getResponsibilities())
                                && w.getYears() != null
                )
                .map(ResumeMapper::toModel)
                .toList();
    }

    private List<ContactInfo> filterContacts(ResumeFormDto dto) {

        return dto.getContactList()
                .stream()
                .filter(c ->
                        c.getTypeId() != null
                                && notBlank(c.getValue())
                )
                .map(ResumeMapper::toModel)
                .toList();
    }

    private boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }

    private <T> void padTo3(List<T> list, Supplier<T> supplier) {

        while (list.size() < EMPTY_ROWS) {
            list.add(supplier.get());
        }
    }
}