package kg.attractor.jobsearch.mapper;

import kg.attractor.jobsearch.dto.UserRegistrationDto;
import kg.attractor.jobsearch.dto.UserResponseDto;
import kg.attractor.jobsearch.model.User;

public class UserMapper {

    private UserMapper() {
    }

    public static User toModel(UserRegistrationDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setAge(dto.getAge());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAvatar(dto.getAvatar());
        user.setAccountType(dto.getAccountType());
        return user;
    }

    public static UserResponseDto toDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setAge(user.getAge());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAvatar(user.getAvatar());
        dto.setAccountType(user.getAccountType());
        return dto;
    }
}