package kg.attractor.jobsearch.controller;

import kg.attractor.jobsearch.model.ModelUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/registration")
    public ResponseEntity<ModelUser> registration(@RequestBody ModelUser user) {
        return ResponseEntity.ok(user);
    }
}