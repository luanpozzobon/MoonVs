package luan.moonvs.controllers;

import luan.moonvs.models.requests.ProfileRequest;
import luan.moonvs.models.responses.ProfileResponse;
import luan.moonvs.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService service;

    @PostMapping("/new")
    public ResponseEntity<String> createProfile(@RequestBody ProfileRequest profileRequest) {
        return service.createProfile(profileRequest);
    }

    @GetMapping("/")
    public ResponseEntity<ProfileResponse> seeProfile() {
        return service.profileInfo();
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editProfile(@RequestBody ProfileRequest profileRequest) {
        return service.editProfile(profileRequest);
    }
}
