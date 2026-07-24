package kg.attractor.jobsearch.controller;

import kg.attractor.jobsearch.dto.UserProfileUpdateDto;
import kg.attractor.jobsearch.dto.UserResponseDto;
import kg.attractor.jobsearch.mapper.UserMapper;
import kg.attractor.jobsearch.model.User;
import kg.attractor.jobsearch.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(UserMapper.toDto(userService.getUserById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateProfile(@PathVariable Long id,
                                                         @RequestBody UserProfileUpdateDto dto) {
        User updates = UserMapper.toModel(dto);
        return ResponseEntity.ok(UserMapper.toDto(userService.updateProfile(id, updates)));
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<UserResponseDto>> searchByName(@RequestParam String name) {
        List<UserResponseDto> result = userService.findByName(name).stream()
                .map(UserMapper::toDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/phone")
    public ResponseEntity<UserResponseDto> searchByPhone(@RequestParam String phoneNumber) {
        return ResponseEntity.ok(UserMapper.toDto(userService.findByPhoneNumber(phoneNumber)));
    }

    @GetMapping("/search/email")
    public ResponseEntity<UserResponseDto> searchByEmail(@RequestParam String email) {
        return ResponseEntity.ok(UserMapper.toDto(userService.findByEmail(email)));
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }

    @PostMapping("/{id}/avatar")
    public ResponseEntity<Void> uploadAvatar(@PathVariable Long id,
                                             @RequestParam("file") MultipartFile file) {
        userService.uploadAvatar(id, file);
        return ResponseEntity.ok().build();
    }
}