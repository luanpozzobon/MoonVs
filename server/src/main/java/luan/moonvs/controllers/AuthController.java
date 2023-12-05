package luan.moonvs.controllers;

import luan.moonvs.models.requests.AuthRequest;
import luan.moonvs.models.responses.AuthResponse;
import luan.moonvs.models.requests.RegisterRequest;
import luan.moonvs.models.responses.RegisterResponse;
import luan.moonvs.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping( { "/", "/login" } )
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authDTO) {
        return authService.login(authDTO);
    }

    @PostMapping("/register")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerDTO) {
        return authService.register(registerDTO);
    }
}
