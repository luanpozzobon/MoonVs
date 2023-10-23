package luan.moonvs.controllers;

import luan.moonvs.models.requests.AuthRequestDTO;
import luan.moonvs.models.responses.AuthResponseDTO;
import luan.moonvs.models.requests.RegisterRequestDTO;
import luan.moonvs.models.responses.RegisterResponseDTO;
import luan.moonvs.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping( { "/", "/login" } )
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authDTO) {
        return authService.login(authDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO registerDTO) {
        return authService.register(registerDTO);
    }
}
