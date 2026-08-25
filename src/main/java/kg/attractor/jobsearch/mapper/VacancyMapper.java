package kg.attractor.jobsearch.mapper;

import kg.attractor.jobsearch.dto.VacancyRequestDto;
import kg.attractor.jobsearch.dto.VacancyResponseDto;
import kg.attractor.jobsearch.model.Category;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.model.Vacancy;

public class VacancyMapper {

    private VacancyMapper() {
    }

    public static Vacancy toModel(VacancyRequestDto dto) {
        Vacancy vacancy = new Vacancy();
        vacancy.setName(dto.getName());
        vacancy.setDescription(dto.getDescription());

        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            vacancy.setCategory(category);
        }

        vacancy.setSalary(dto.getSalary());
        vacancy.setExpFrom(dto.getExpFrom());
        vacancy.setExpTo(dto.getExpTo());
        vacancy.setIsActive(dto.getIsActive());

        if (dto.getAuthorId() != null) {
            User author = new User();
            author.setId(dto.getAuthorId());
            vacancy.setAuthor(author);
        }

        return vacancy;
    }

    public static VacancyResponseDto toDto(Vacancy vacancy) {
        VacancyResponseDto dto = new VacancyResponseDto();
        dto.setId(vacancy.getId());
        dto.setName(vacancy.getName());
        dto.setDescription(vacancy.getDescription());
        dto.setCategoryId(vacancy.getCategory() != null ? vacancy.getCategory().getId() : null);
        dto.setSalary(vacancy.getSalary());
        dto.setExpFrom(vacancy.getExpFrom());
        dto.setExpTo(vacancy.getExpTo());
        dto.setIsActive(vacancy.getIsActive());
        dto.setAuthorId(vacancy.getAuthor() != null ? vacancy.getAuthor().getId() : null);
        dto.setCreatedDate(vacancy.getCreatedDate());
        dto.setUpdateTime(vacancy.getUpdateTime());
        return dto;
    }
}