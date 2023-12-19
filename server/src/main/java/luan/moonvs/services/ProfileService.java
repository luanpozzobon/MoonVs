package luan.moonvs.services;

import jakarta.persistence.EntityNotFoundException;
import luan.moonvs.models.builders.ProfileBuilder;
import luan.moonvs.models.entities.Profile;
import luan.moonvs.models.entities.User;
import luan.moonvs.models.requests.ProfileRequest;
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


    @Autowired
    public ProfileService(ProfileRepository repository, ProfileBuilder builder, AccountService accountService) {
        this.repository = repository;
        this.builder = builder;
        this.accountService = accountService;
    }

    public ResponseEntity<String> createOrEditProfile(ProfileRequest profileRequest) {
        User authUser = accountService.getAuthenticatedUser();

        try {
            Profile profile = builder
                    .fromAuthUser(authUser)
                    .createOrEditProfile(profileRequest)
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

    public ResponseEntity<Profile> profileInfo() {
        User authUser = accountService.getAuthenticatedUser();
        try {
            Profile profile = repository.getReferenceById(authUser.getIdUser());

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(profile);

        } catch (EntityNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }
}
