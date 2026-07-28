package kg.attractor.jobsearch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfoDto {

    private Long id;

    @NotNull(message = "Тип контакта обязателен")
    private Long typeId;

    @NotBlank(message = "Значение контакта обязательно для заполнения")
    private String value;
}