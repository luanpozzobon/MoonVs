package luan.moonvs.controllers;

import luan.moonvs.models.requests.AuthRequest;
import luan.moonvs.models.requests.UserAccountRequest;
import luan.moonvs.models.responses.AuthResponse;
import luan.moonvs.models.requests.RegisterRequest;
import luan.moonvs.models.responses.Response;
import luan.moonvs.models.responses.UserAccountResponse;
import luan.moonvs.models.responses.UserAuthData;
import luan.moonvs.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService service;
    private final String HEADER_NAME = "message";

    @Autowired
    private AuthController(AuthService service) {
        this.service = service;
    }

    @Deprecated
    @PostMapping( { "/", "/login" } )
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authDTO) {
        return service.login(authDTO);
    }

    @PostMapping( { "/sign-in" } )
    public ResponseEntity<UserAuthData> login(@RequestBody UserAccountRequest userAccount) {
        final String BAD_REQUEST = "You need to fill both username and password!";
        if ((userAccount.username() == null || userAccount.username().isBlank())
        ||  (userAccount.password() == null || userAccount.password().isBlank()))
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .header(HEADER_NAME, BAD_REQUEST)
                    .build();

        var response = service.login(userAccount.username(), userAccount.password());
        return ResponseEntity
                .status(response.status())
                .header(HEADER_NAME, response.message())
                .body(response.entity());
    }

    @Deprecated
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerDTO) {
        return service.register(registerDTO);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserAuthData> register(@RequestBody UserAccountRequest userAccount) {
        var response = service.register(userAccount);

        return ResponseEntity
                .status(response.status())
                .header(HEADER_NAME, response.message())
                .body(response.entity());
    }
}
