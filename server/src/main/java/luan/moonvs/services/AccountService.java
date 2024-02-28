package luan.moonvs.services;

import luan.moonvs.models.builders.UserBuilder;
import luan.moonvs.models.entities.User;
import luan.moonvs.models.responses.Response;
import luan.moonvs.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final UserRepository repository;
    private final String SUCCESS_MESSAGE = "User updated successfully";

    @Autowired
    private AccountService(UserRepository repository) {
        this.repository = repository;
    }

    public User getAccount() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return repository.getUserByUsername(userDetails.getUsername());
    }

    public Response<User> updateUsernameAndOrEmail(String username, String email, User user) {
        try {
            user = UserBuilder.create(repository, user)
                    .withUsername(username)
                    .withEmail(email)
                    .build();

            repository.save(user);
            return new Response<>(HttpStatus.OK, user, SUCCESS_MESSAGE);
        } catch (IllegalArgumentException e) {
            return new Response<>(HttpStatus.BAD_REQUEST, new User(), e.getMessage());
        }
    }

    public Response<User> updatePassword(String password, User user) {
        try {
            user = UserBuilder.create(repository, user)
                    .withPassword(password)
                    .build();

            repository.save(user);
            return new Response<>(HttpStatus.OK, new User(), SUCCESS_MESSAGE);
        } catch (IllegalArgumentException e) {
            return new Response<>(HttpStatus.BAD_REQUEST, new User(), e.getMessage());
        }
    }

    public Response<User> deleteAccount(String username, String password, User user) {
        final String INVALID_FIELD = "The filled %s doesn't match!",
                     DELETE_SUCCESS = "The account was successfully deleted!";

        if (!username.equals(user.getUsername()))
            return new Response<>(HttpStatus.BAD_REQUEST, new User(), String.format(INVALID_FIELD, "username"));


        if (!(new BCryptPasswordEncoder().matches(password, user.getPassword())))
            return new Response<>(HttpStatus.BAD_REQUEST, String.format(INVALID_FIELD, password));

        repository.delete(user);
        return new Response<>(HttpStatus.OK, new User(), DELETE_SUCCESS);
    }
}