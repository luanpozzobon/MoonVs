package luan.moonvs.services;

import jakarta.persistence.EntityNotFoundException;
import luan.moonvs.models.builders.ProfileBuilder;
import luan.moonvs.models.entities.Profile;
import luan.moonvs.models.entities.User;
import luan.moonvs.models.requests.ProfileRequest;
import luan.moonvs.models.responses.Response;
import luan.moonvs.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileService {
    private final ProfileRepository repository;
    private final AccountService accountService;

    private final String ID_DOESNT_MATCH = "It is not possible to create, or edit another user's profile!",
                         NOT_FOUND = "The requested profile wasn't found!";

    @Autowired
    public ProfileService(ProfileRepository repository, AccountService accountService) {
        this.repository = repository;
        this.accountService = accountService;
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

    public Response<Profile> getProfile(UUID idUser) {
        try {
            Profile profile = repository.getReferenceById(idUser);

            return new Response<>(HttpStatus.OK, profile);
        } catch (EntityNotFoundException e) {
            return new Response<>(HttpStatus.NOT_FOUND, new Profile(), NOT_FOUND);
        }
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