package lpz.moonvs.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "lpz.moonvs")
public class MoonVs {

    public static void main(String[] args) {
        SpringApplication.run(MoonVs.class, args);
    }

}
