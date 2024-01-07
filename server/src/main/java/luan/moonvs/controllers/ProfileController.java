package luan.moonvs.controllers;

import luan.moonvs.models.requests.ProfileRequest;
import luan.moonvs.models.responses.ProfileResponse;
import luan.moonvs.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService service;

    private final String HEADER_NAME = "message",
                         MISSING_ID = "It is necessary the Id of the user!";

    @Deprecated
    @PostMapping("/new")
    public ResponseEntity<String> createProfile(@RequestBody ProfileRequest profileRequest) {
        return service.createProfile(profileRequest);
    }

    @PostMapping("/create/{idUser}")
    public ResponseEntity<ProfileResponse> createProfile(@PathVariable UUID idUser, @RequestBody ProfileRequest profileRequest) {
        if (idUser == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header("message", MISSING_ID)
                    .build();

        var response = service.createProfile(idUser, profileRequest);
        return ResponseEntity
                .status(response.status())
                .header(HEADER_NAME, response.message())
                .body(new ProfileResponse(response.entity()));
    }

    @Deprecated
    @GetMapping("/")
    public ResponseEntity<ProfileResponse> seeProfile() {
        return service.profileInfo();
    }

    @GetMapping("/{idUser}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable UUID idUser) {
        if (idUser == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, MISSING_ID)
                    .build();

        var response = service.getProfile(idUser);
        return ResponseEntity
                .status(response.status())
                .header(HEADER_NAME, response.message())
                .body(new ProfileResponse(response.entity()));
    }

    @Deprecated
    @PutMapping("/edit")
    public ResponseEntity<String> editProfile(@RequestBody ProfileRequest profileRequest) {
        return service.editProfile(profileRequest);
    }

    @PutMapping("/edit/{idUser}")
    public ResponseEntity<ProfileResponse> editProfile(@PathVariable UUID idUser, @RequestBody ProfileRequest profileRequest) {
        if (idUser == null)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, MISSING_ID)
                    .build();

        var response = service.editProfile(idUser, profileRequest);
        return ResponseEntity
                .status(response.status())
                .header(HEADER_NAME, response.message())
                .body(new ProfileResponse(response.entity()));
    }
}
