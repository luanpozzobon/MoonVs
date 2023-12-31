package luan.moonvs.services;

import luan.moonvs.models.builders.UserBuilder;
import luan.moonvs.models.entities.User;
import luan.moonvs.models.requests.AccountRequest;
import luan.moonvs.models.requests.AuthRequest;
import luan.moonvs.models.responses.AccountResponse;
import luan.moonvs.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    private final UserRepository repository;
    private final UserBuilder builder;

    @Autowired
    private AccountService(UserRepository repository, UserBuilder builder) {
        this.repository = repository;
        this.builder = builder;
    }

    public ResponseEntity<AccountResponse> account() {
        final User authUser = getAuthenticatedUser();
        AccountResponse account = new AccountResponse(authUser);
        return ResponseEntity.ok(account);
    }

    public ResponseEntity<AccountResponse> updateAccount(AccountRequest accountRequest) {
        final String SUCCESS = "Usuário alterado com sucesso!";
        final User authUser = getAuthenticatedUser();

        try {
            User user = builder
                    .fromAuthUser(authUser)
                    .withUsername(accountRequest.username())
                    .withEmail(accountRequest.email())
                    .build();

            repository.save(user);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new AccountResponse(authUser, SUCCESS));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new AccountResponse(e.getMessage()));
        }
    }

    public ResponseEntity<String> updatePassword(String password) {
        final String SUCCESS = "Senha atualizada com sucesso!",
                EMPTY_PASSWORD = "Digite uma senha para continuar!";
        final User authUser = getAuthenticatedUser();

        if (password == null || password.isBlank())
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(EMPTY_PASSWORD);

        try {
            User user = builder
                    .fromAuthUser(authUser)
                    .withPassword(password)
                    .build();

            repository.save(user);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(SUCCESS);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    public ResponseEntity<String> deleteAccount(AuthRequest authRequest) {
        final String EMPTY_STRING = "Forneça %s para prosseguir com a solicitação!";
        User authUser = getAuthenticatedUser();
        if (authRequest.username() == null || authRequest.username().isBlank())
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(String.format(EMPTY_STRING, "o 'Username'"));

        if (authRequest.password() == null || authRequest.password().isBlank())
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(String.format(EMPTY_STRING, "a 'Senha'"));

        if (!authRequest.username().equals(authUser.getUsername()))
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("'Username' inválido");

        if (!new BCryptPasswordEncoder().matches(authRequest.password(), authUser.getPassword()))
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("'Senha' inválida");

        repository.delete(authUser);
        return ResponseEntity.status(HttpStatus.OK).body("Conta deletada com sucesso!");
    }

    protected User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return repository.getUserByUsername(userDetails.getUsername());
    }
}