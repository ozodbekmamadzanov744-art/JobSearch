package kg.attractor.jobsearch.controller;

import kg.attractor.jobsearch.dto.UserRegistrationDto;
import kg.attractor.jobsearch.dto.UserResponseDto;
import kg.attractor.jobsearch.mapper.UserMapper;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<UserResponseDto> registration(@RequestBody UserRegistrationDto dto) {
        User user = UserMapper.toModel(dto);
        User registered = userService.register(user);
        return ResponseEntity.ok(UserMapper.toDto(registered));
    }
}