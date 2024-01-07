package luan.moonvs.services;

import jakarta.persistence.EntityNotFoundException;
import luan.moonvs.models.builders.ProfileBuilder;
import luan.moonvs.models.entities.Profile;
import luan.moonvs.models.entities.User;
import luan.moonvs.models.requests.ProfileRequest;
import luan.moonvs.models.responses.ProfileResponse;
import luan.moonvs.models.responses.Response;
import luan.moonvs.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileService {
    private final ProfileRepository repository;

    @Deprecated
    private final ProfileBuilder builder;
    private final AccountService accountService;
    private final ContentService contentService;

    private final String ID_DOESNT_MATCH = "It is not possible to create, or edit another user's profile!",
                         NOT_FOUND = "The requested profile wasn't found!";

    @Deprecated
    @Autowired
    public ProfileService(ProfileRepository repository, ProfileBuilder builder, AccountService accountService, ContentService contentService) {
        this.repository = repository;
        this.builder = builder;
        this.accountService = accountService;
        this.contentService = contentService;
    }

    @Deprecated
    public ResponseEntity<String> createProfile(ProfileRequest profileRequest) {
        User authUser = accountService.getAccount();

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

    public Response<Profile> createProfile(UUID idUser, ProfileRequest profileRequest) {
        User user = accountService.getAccount();
        if (!idUser.equals(user.getIdUser()))
            return new Response<>(HttpStatus.FORBIDDEN, new Profile(), ID_DOESNT_MATCH);

        try {
            Profile profile = ProfileBuilder.create(user)
                    .withBio(profileRequest.biography())
                    .privacy(profileRequest.isPrivate())
                    .build();

            repository.save(profile);
            return new Response<>(HttpStatus.CREATED, profile);
        } catch (IllegalArgumentException e) {
            return new Response<>(HttpStatus.BAD_REQUEST, new Profile(), e.getMessage());
        }
    }

    @Deprecated
    public ResponseEntity<ProfileResponse> profileInfo() {
        User authUser = accountService.getAccount();
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

    public Response<Profile> getProfile(UUID idUser) {
        try {
            Profile profile = repository.getReferenceById(idUser);

            return new Response<>(HttpStatus.OK, profile);
        } catch (EntityNotFoundException e) {
            return new Response<>(HttpStatus.NOT_FOUND, new Profile(), NOT_FOUND);
        }
    }

    @Deprecated
    public ResponseEntity<String> editProfile(ProfileRequest profileRequest) {
        User authUser = accountService.getAccount();
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

    public Response<Profile> editProfile(UUID idUser, ProfileRequest profileRequest) {
        User user = accountService.getAccount();
        if (!idUser.equals(user.getIdUser()))
            return new Response<>(HttpStatus.FORBIDDEN, new Profile(), ID_DOESNT_MATCH);

        try {
            Profile profile = repository.getReferenceById(idUser);

            profile = ProfileBuilder.create(profile)
                    .withBio(profileRequest.biography())
                    .privacy(profileRequest.isPrivate())
                    .build();

            repository.save(profile);
            return new Response<>(HttpStatus.OK, profile);
        } catch (EntityNotFoundException e) {
            return new Response<>(HttpStatus.NOT_FOUND, new Profile(), NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new Response<>(HttpStatus.BAD_REQUEST, new Profile(), e.getMessage());
        }
    }
}
