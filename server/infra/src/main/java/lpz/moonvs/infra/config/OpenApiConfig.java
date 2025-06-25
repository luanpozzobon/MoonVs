package lpz.moonvs.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("MoonVs API")
                .version("1.0.0")
                .termsOfService("http://swagger.io/terms/")
                .contact(new Contact()
                        .name("Luan Pozzobon")
                        .email("luanpozzobon@gmail.com")
                        .url("https://github.com/luanpozzobon"))
                .license(new License()
                        .name("MIT License")
                        .url("https://github.com/luanpozzobon/MoonVs/blob/main/LICENSE"))
        );
    }
}
