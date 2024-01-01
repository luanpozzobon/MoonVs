package luan.moonvs.services;

import jakarta.persistence.EntityNotFoundException;
import luan.moonvs.models.builders.ProfileBuilder;
import luan.moonvs.models.entities.Content;
import luan.moonvs.models.entities.Profile;
import luan.moonvs.models.entities.User;
import luan.moonvs.models.requests.ProfileRequest;
import luan.moonvs.models.responses.ProfileResponse;
import luan.moonvs.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private final ProfileRepository repository;
    private final ProfileBuilder builder;
    private final AccountService accountService;
    private final ContentService contentService;


    @Autowired
    public ProfileService(ProfileRepository repository, ProfileBuilder builder, AccountService accountService, ContentService contentService) {
        this.repository = repository;
        this.builder = builder;
        this.accountService = accountService;
        this.contentService = contentService;
    }

    public ResponseEntity<String> createProfile(ProfileRequest profileRequest) {
        User authUser = accountService.getAuthenticatedUser();

        try {
            Profile profile = builder
                    .createProfile(profileRequest)
                    .fromAuthUser(authUser)
                    .build();

            repository.save(profile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    public ResponseEntity<ProfileResponse> profileInfo() {
        User authUser = accountService.getAuthenticatedUser();
        try {
            Profile profile = repository.getReferenceById(authUser.getIdUser());
            ProfileResponse response = new ProfileResponse(profile);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);

        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    public ResponseEntity<String> editProfile(ProfileRequest profileRequest) {
        User authUser = accountService.getAuthenticatedUser();
        Profile profile = repository.getReferenceById(authUser.getIdUser());

        try {
            profile = builder
                    .editProfile(profile, profileRequest)
                    .fromAuthUser(authUser)
                    .build();

            repository.save(profile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
