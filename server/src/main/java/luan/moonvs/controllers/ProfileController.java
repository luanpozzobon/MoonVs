package luan.moonvs.controllers;

import luan.moonvs.models.entities.Profile;
import luan.moonvs.models.requests.ProfileRequest;
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
    public ResponseEntity<String> createProfile(ProfileRequest profileRequest) {
        return service.createOrEditProfile(profileRequest);
    }

    @GetMapping("/")
    public ResponseEntity<Profile> seeProfile() {
        return service.profileInfo();
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editProfile(ProfileRequest profileRequest) {
        return service.createOrEditProfile(profileRequest);
    }
}
