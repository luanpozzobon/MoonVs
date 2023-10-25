package luan.moonvs;

import luan.moonvs.models.requests.RegisterRequestDTO;
import luan.moonvs.repositories.UserRepository;
import luan.moonvs.services.AccountService;
import luan.moonvs.services.AuthService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class MoonVs {
    public static void main(String[] args) {
        SpringApplication.run(MoonVs.class, args);
    }
}
