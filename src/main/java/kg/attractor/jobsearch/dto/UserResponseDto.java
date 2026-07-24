package kg.attractor.jobsearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String name;
    private String surname;
    private Integer age;
    private String email;
    private String phoneNumber;
    private String avatar;
    private String accountType;
}