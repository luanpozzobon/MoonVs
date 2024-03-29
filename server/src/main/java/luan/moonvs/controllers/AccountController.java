package luan.moonvs.controllers;

import luan.moonvs.models.entities.User;
import luan.moonvs.models.exceptions.IllegalIdException;
import luan.moonvs.models.requests.UserAccountRequest;
import luan.moonvs.models.responses.AccountResponse;
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
    private final String HEADER_NAME = "message";

    @Autowired
    public AccountController(AccountService service) {
        this.service = service;
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

            var response = service.updateUsernameAndOrEmail(userAccount.username(), userAccount.email(), user);
            return ResponseEntity
                    .status(response.status())
                    .header(HEADER_NAME, response.message())
                    .body(new AccountResponse(response.entity()));

        } catch (IllegalIdException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, e.message)
                    .build();
        }
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

            var response = service.updatePassword(userAccount.password(), user);
            return ResponseEntity
                    .status(response.status())
                    .header(HEADER_NAME, response.message())
                    .body(new AccountResponse(response.entity()));

        } catch (IllegalIdException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, e.getMessage())
                    .build();
        }
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<AccountResponse> deleteAccount(@RequestBody UserAccountRequest userAccount) {
        final String EMPTY_REQUEST = "You must fill the %s field";

        try {
            User user = checkIdAndReturnUser(userAccount.idUser());

            if (userAccount.username() == null || userAccount.username().isBlank())
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .header(HEADER_NAME, String.format(EMPTY_REQUEST, "username"))
                        .build();

            if (userAccount.password() == null || userAccount.password().isBlank())
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .header(HEADER_NAME, String.format(EMPTY_REQUEST, "password"))
                        .build();

            var response = service.deleteAccount(userAccount.username(), userAccount.password(), user);
            return ResponseEntity
                    .status(response.status())
                    .header(HEADER_NAME, response.message())
                    .body(new AccountResponse(response.entity()));

        } catch (IllegalIdException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, e.message)
                    .build();
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