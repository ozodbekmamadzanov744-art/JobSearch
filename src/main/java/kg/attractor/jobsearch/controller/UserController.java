package kg.attractor.jobsearch.controller;

import kg.attractor.jobsearch.model.ModelUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public ResponseEntity<ModelUser> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(new ModelUser());
    }

    @PostMapping("/{id}/avatar")
    public ResponseEntity<Void> uploadAvatar(@PathVariable Long id,
                                             @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().build();
    }
}