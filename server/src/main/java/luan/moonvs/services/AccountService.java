package luan.moonvs.services;

import luan.moonvs.models.builders.UserBuilder;
import luan.moonvs.models.entities.User;
import luan.moonvs.models.requests.AccountRequestDTO;
import luan.moonvs.models.requests.DeleteRequestDto;
import luan.moonvs.models.requests.PasswordRequestDTO;
import luan.moonvs.models.responses.AccountResponseDTO;
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
    public AccountService(UserRepository repository, UserBuilder builder) {
        this.repository = repository;
        this.builder = builder;
    }

    public ResponseEntity<AccountResponseDTO> account() {
        User authUser = getAuthenticatedUser();
        AccountResponseDTO accountDto = new AccountResponseDTO(authUser);
        return ResponseEntity.ok(accountDto);
    }

    public ResponseEntity<AccountResponseDTO> updateAccount(AccountRequestDTO accountRequestDTO) {
        final String SUCCESS = "Usuário alterado com sucesso!";
        final User authUser = getAuthenticatedUser();
        String username, email;

        if (accountRequestDTO.username() != null && !accountRequestDTO.username().isBlank())
            username = accountRequestDTO.username();
        else
            username = authUser.getUsername();

        if (accountRequestDTO.email() != null && !accountRequestDTO.email().isBlank())
            email = accountRequestDTO.email();
        else
            email = authUser.getEmail();

        try {
            User user = builder
                    .fromAuthUser(authUser)
                    .withUsername(username)
                    .withEmail(email)
                    .build();

            repository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(new AccountResponseDTO(authUser, SUCCESS));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AccountResponseDTO(e.getMessage()));
        }
    }

    public ResponseEntity<String> updatePassword(PasswordRequestDTO passwordRequestDTO) {
        final String SUCCESS = "Senha atualizada com sucesso!",
                EMPTY_PASSWORD = "Digite uma senha para continuar!";
        final User authUser = getAuthenticatedUser();

        if (passwordRequestDTO.password().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(EMPTY_PASSWORD);

        try {
            User user = builder
                    .withPassword(passwordRequestDTO)
                    .build();

            repository.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(SUCCESS);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    public ResponseEntity<String> deleteAccount(DeleteRequestDto deleteRequestDto) {
        final String EMPTY_STRING = "Forneça %s para prosseguir com a solicitação!";
        User authUser = getAuthenticatedUser();
        if (deleteRequestDto.username().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format(EMPTY_STRING, "o 'Username'"));

        if (deleteRequestDto.password().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format(EMPTY_STRING, "a 'Senha'"));

        if (!deleteRequestDto.username().equals(authUser.getUsername()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("'Username' inválido");

        if (!new BCryptPasswordEncoder().matches(deleteRequestDto.password(), authUser.getPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("'Senha' inválida");

        repository.delete(authUser);
        return ResponseEntity.status(HttpStatus.OK).body("Conta deletada com sucesso!");
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return repository.findUserByUsername(userDetails.getUsername());
    }
}