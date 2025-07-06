package lpz.moonvs.api.auth;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lpz.moonvs.api.auth.input.LoginInput;
import lpz.moonvs.api.auth.input.RegisterInput;
import lpz.moonvs.application.auth.output.RegisterOutput;
import lpz.moonvs.domain.auth.contracts.IUserRepository;
import lpz.moonvs.domain.auth.entity.User;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerTest {
    private static final String REGISTER_PATH = "/auth/register";
    private static final String LOGIN_PATH = "/auth/login";

    private static final String VALID_USERNAME = "luanpozzobon";
    private static final String VALID_EMAIL = "luanpozzobon@gmail.com";
    private static final String VALID_PASSWORD = "M00n_Vs.";

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.13").withDatabaseName("mvs-test").withUsername("test-user").withPassword("test-password");

    @DynamicPropertySource
    static void postgreSQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @LocalServerPort
    private Integer port;

    @Autowired
    private IUserRepository repository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void shouldRegisterSuccessfully() {
        final var input = new RegisterInput(VALID_EMAIL, VALID_USERNAME, VALID_PASSWORD);

        final RegisterOutput output =
                given()
                        .contentType(ContentType.JSON)
                        .body(input)
                .when()
                        .post(REGISTER_PATH)
                .then()
                        .statusCode(CREATED.value())
                        .header("Set-Cookie", containsString("token="))
                        .body(User.Schema.ID, is(notNullValue()))
                        .body(User.Schema.USERNAME, equalTo(VALID_USERNAME))
                        .body(User.Schema.EMAIL, equalTo(VALID_EMAIL))
                        .extract().body().as(RegisterOutput.class);

        final Optional<User> userOpt = this.repository.findById(Id.from(output.id()));
        assertTrue(userOpt.isPresent());
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() {
        final var anInput = new RegisterInput(VALID_EMAIL, VALID_USERNAME, VALID_PASSWORD);

        given().contentType(ContentType.JSON).body(anInput)
                .when().post(REGISTER_PATH)
                .then().statusCode(CREATED.value());

        final String username = "luanpozzobon1";
        final var otherInput = new RegisterInput(VALID_EMAIL, username, VALID_PASSWORD);

        given()
                .contentType(ContentType.JSON)
                .body(otherInput)
        .when()
                .post("/auth/register")
        .then()
                .statusCode(CONFLICT.value())
                .body("detail", equalTo("There is already an user registered with these info"));

    }

    @Test
    void shouldReturnConflictWhenUsernameAlreadyExists() {
        final var anInput = new RegisterInput(VALID_EMAIL, VALID_USERNAME, VALID_PASSWORD);

        given().contentType(ContentType.JSON).body(anInput)
                .when().post(REGISTER_PATH)
                .then().statusCode(CREATED.value());

        final String email = "luanpozzobon1@gmail.com";
        final var otherInput = new RegisterInput(email, VALID_USERNAME, VALID_PASSWORD);

        given()
                .contentType(ContentType.JSON)
                .body(otherInput)
        .when()
                .post("/auth/register")
        .then()
                .statusCode(CONFLICT.value())
                .body("detail", equalTo("There is already an user registered with these info"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n", "luanpozzobon", "@gmail.com", "luanpozzobon@gmail", "luanpozzobon.com"})
    void shouldReturnBadRequestWhenEmailIsInvalid(String invalidEmail) {
        final var input = new RegisterInput(invalidEmail, VALID_USERNAME, VALID_PASSWORD);

        given()
                .contentType(ContentType.JSON)
                .body(input)
        .when()
                .post(REGISTER_PATH)
        .then()
                .statusCode(BAD_REQUEST.value())
                .body("detail", equalTo("Failed to create"));
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n", "l", "lp", "lpz", "luan@pozzobon"})
    void shouldReturnBadRequestWhenUsernameIsInvalid(String invalidUsernam) {
        final var input = new RegisterInput(VALID_EMAIL, invalidUsernam, VALID_PASSWORD);

        given()
                .contentType(ContentType.JSON)
                .body(input)
        .when()
                .post(REGISTER_PATH)
        .then()
                .statusCode(BAD_REQUEST.value())
                .body("detail", equalTo("Failed to create"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n", "M00n_Vs", "m00n_vs.", "M00N_VS.", "Moon_Vs.", "M00nVs12"})
    void shouldReturnBadRequestWhenPasswordIsInvalid(String invalidPassword) {
        final var input = new RegisterInput(VALID_EMAIL, VALID_USERNAME, invalidPassword);

        given()
                .contentType(ContentType.JSON)
                .body(input)
        .when()
                .post(REGISTER_PATH)
        .then()
                .statusCode(BAD_REQUEST.value())
                .body("detail", equalTo("Failed to create"));
    }

    @Test
    void shouldLoginSuccessfully() {
        final var registerInput = new RegisterInput(VALID_EMAIL, VALID_USERNAME, VALID_PASSWORD);

        given().contentType(ContentType.JSON).body(registerInput)
                .when().post(REGISTER_PATH)
                .then().statusCode(CREATED.value());

        final var input = new LoginInput(VALID_USERNAME, VALID_PASSWORD);


        given()
                .contentType(ContentType.JSON)
                .body(input)
        .when()
                .post(LOGIN_PATH)
        .then()
                .statusCode(OK.value())
                .header("Set-Cookie", containsString("token="))
                .body(User.Schema.ID, is(notNullValue()))
                .body(User.Schema.USERNAME, equalTo(VALID_USERNAME))
                .body(User.Schema.EMAIL, equalTo(VALID_EMAIL));
    }

    @Test
    void shouldReturnNotFoundWhenUsernameDoesNotExists() {
        final var registerInput = new RegisterInput(VALID_EMAIL, VALID_USERNAME, VALID_PASSWORD);

        given().contentType(ContentType.JSON).body(registerInput)
                .when().post(REGISTER_PATH)
                .then().statusCode(CREATED.value());

        final var input = new LoginInput("luanpozzobon1", VALID_PASSWORD);

        given()
                .contentType(ContentType.JSON)
                .body(input)
        .when()
                .post(LOGIN_PATH)
        .then()
                .statusCode(NOT_FOUND.value())
                .body("detail", equalTo("There is no user registered with these credentials."));
    }

    @Test
    void shouldReturnNotFoundWhenPasswordDoesNotExists() {
        final var registerInput = new RegisterInput(VALID_EMAIL, VALID_USERNAME, VALID_PASSWORD);

        given().contentType(ContentType.JSON).body(registerInput)
                .when().post(REGISTER_PATH)
                .then().statusCode(CREATED.value());

        final var input = new LoginInput(VALID_USERNAME, "p@ssw0rd");

        given()
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .post(LOGIN_PATH)
                .then()
                .statusCode(NOT_FOUND.value())
                .body("detail", equalTo("There is no user registered with these credentials."));
    }
}
