package luan.moonvs.controllers;

import luan.moonvs.models.requests.AuthRequestDTO;
import luan.moonvs.models.responses.AuthResponseDTO;
import luan.moonvs.models.requests.RegisterRequestDTO;
import luan.moonvs.models.responses.RegisterResponseDTO;
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
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authDTO) {
        return authService.login(authDTO);
    }

    @PostMapping("/register")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO registerDTO) {
        return authService.register(registerDTO);
    }
}
