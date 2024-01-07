package luan.moonvs.controllers;

import luan.moonvs.models.entities.User;
import luan.moonvs.models.exceptions.IllegalIdException;
import luan.moonvs.models.requests.AccountRequest;
import luan.moonvs.models.requests.AuthRequest;
import luan.moonvs.models.requests.UserAccountRequest;
import luan.moonvs.models.responses.AccountResponse;
import luan.moonvs.models.responses.UserAccountResponse;
import luan.moonvs.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService service;

    @Autowired
    public AccountController(AccountService service) {
        this.service = service;
    }

    @Deprecated
    @GetMapping("/")
    public ResponseEntity<AccountResponse> account() {
        return service.account();
    }

    @GetMapping("/{idUser}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable UUID idUser) {
        try {
            User user = checkIdAndReturnUser(idUser);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new AccountResponse(user));
        } catch (IllegalIdException e) {
            return  ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new AccountResponse(e.message));
        }
    }

    @Deprecated
    @PatchMapping("/update")
    public ResponseEntity<AccountResponse> updateAccount(@RequestBody AccountRequest accountDto) {
        return service.updateAccount(accountDto);
    }

    @PatchMapping("/update/username-and-email")
    public ResponseEntity<AccountResponse> updateUsernameAndOrEmail(@RequestBody UserAccountRequest userAccount) {
        final String EMPTY_REQUEST = "At least one of the fields must be filled!";

        try {
            User user = checkIdAndReturnUser(userAccount.idUser());

            if ((userAccount.username() == null || userAccount.username().isBlank()) &&
                (userAccount.email() == null || userAccount.email().isBlank())) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new AccountResponse(EMPTY_REQUEST));
            }

            UserAccountResponse response = service.updateUsernameAndOrEmail(userAccount.username(), userAccount.email(), user);
            return ResponseEntity
                    .status(response.status())
                    .body(new AccountResponse(response));

        } catch (IllegalIdException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new AccountResponse(e.message));
        }
    }

    @Deprecated
    @PatchMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody String password) {
        return service.updatePassword(password);
    }

    @PatchMapping("/update/password")
    public ResponseEntity<AccountResponse> updatePassword(@RequestBody UserAccountRequest userAccount) {
        final String EMPTY_REQUEST = "You must fill the password!";

        try {
            User user = checkIdAndReturnUser(userAccount.idUser());

            if (userAccount.password() == null || userAccount.password().isBlank())
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new AccountResponse(EMPTY_REQUEST));

            UserAccountResponse response = service.updatePassword(userAccount.password(), user);
            return ResponseEntity
                    .status(response.status())
                    .body(new AccountResponse(response));

        } catch (IllegalIdException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new AccountResponse(e.message));
        }
    }

    @Deprecated
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccount(@RequestBody AuthRequest authRequest) {
        return service.deleteAccount(authRequest);
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<AccountResponse> deleteAccount(@RequestBody UserAccountRequest userAccount) {
        final String EMPTY_REQUEST = "You must fill the %s field";

        try {
            User user = checkIdAndReturnUser(userAccount.idUser());

            if (userAccount.username() == null || userAccount.username().isBlank())
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new AccountResponse(String.format(EMPTY_REQUEST, "username")));

            if (userAccount.password() == null || userAccount.password().isBlank())
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new AccountResponse(String.format(EMPTY_REQUEST, "password")));

            UserAccountResponse response = service.deleteAccount(userAccount.username(), userAccount.password(), user);
            return ResponseEntity
                    .status(response.status())
                    .body(new AccountResponse(response));

        } catch (IllegalIdException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new AccountResponse(e.message));
        }
    }

    private User checkIdAndReturnUser(UUID id) throws IllegalIdException {
        final String MISSING_ID = "It is necessary the Id of the user!";
        final String NOT_MATCHING_ID = "It is not possible to access a not authenticated account";

        if (id == null)
            throw new IllegalIdException(MISSING_ID);

        User user = service.getAccount();
        if (!id.equals(user.getIdUser()))
            throw new IllegalIdException(NOT_MATCHING_ID);

        return user;
    }
}
