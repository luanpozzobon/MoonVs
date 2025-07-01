package lpz.moonvs.api.playlist;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lpz.moonvs.api.auth.input.RegisterInput;
import lpz.moonvs.api.playlist.input.AddTitleToPlaylistInput;
import lpz.moonvs.api.playlist.input.CreatePlaylistInput;
import lpz.moonvs.api.playlist.input.UpdatePlaylistInput;
import lpz.moonvs.application.playlist.output.CreatePlaylistOutput;
import lpz.moonvs.domain.playlist.contracts.IPlaylistItemRepository;
import lpz.moonvs.domain.playlist.contracts.IPlaylistRepository;
import lpz.moonvs.domain.playlist.entity.Playlist;
import lpz.moonvs.domain.seedwork.valueobject.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
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
import java.util.UUID;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PlaylistControllerTest {
    private static final String PLAYLIST_PATH = "/playlists/";

    private static final String VALID_TITLE = "Playlist";
    private static final String VALID_DESCRIPTION = "Description";

    private static final int MAX_TITLE_LENGTH = 64;
    private static final int MAX_DESCRIPTION_LENGTH = 255;

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
    private IPlaylistRepository playlistRepository;

    @Autowired
    private IPlaylistItemRepository playlistItemRepository;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        final var registerInput = new RegisterInput("luanpozzobon@gmail.com", "luanpozzobon", "M00n_Vs.");
        this.token =
            given()
                    .contentType(ContentType.JSON)
                    .body(registerInput)
            .when()
                    .post("/auth/register")
            .then()
                    .statusCode(CREATED.value())
                    .extract().cookie("token");
    }

    private static Stream<Arguments> invalidTitleProvider() {
        return Stream.of(
                Arguments.of(" "),
                Arguments.of("\t"),
                Arguments.of("\n"),
                Arguments.of("a".repeat(MAX_TITLE_LENGTH + 1))
        );
    }

    private static Stream<Arguments> invalidDescriptionProvider() {
        return Stream.of(
                Arguments.of("a".repeat(MAX_DESCRIPTION_LENGTH + 1))
        );
    }

    @Test
    void shouldCreatePlaylistSuccessfully() {
        final var input = new CreatePlaylistInput(VALID_TITLE, VALID_DESCRIPTION);

        final var output =
                given()
                        .contentType(ContentType.JSON)
                        .cookie("token", this.token)
                        .body(input)
                .when()
                        .post(PLAYLIST_PATH)
                .then()
                        .statusCode(CREATED.value())
                        .header("Location", containsString("/playlists/"))
                        .body("id", is(notNullValue()))
                        .body("title", equalTo(VALID_TITLE))
                        .body("description", equalTo(VALID_DESCRIPTION))
                        .extract().body().as(CreatePlaylistOutput.class);

        final Optional<Playlist> playlistOpt = this.playlistRepository.findById(Id.from(output.id()));
        assertTrue(playlistOpt.isPresent());
    }

    @Test
    void shouldReturnConflictWhenPlaylistTitleAlreadyExists() {
        final var input = new CreatePlaylistInput(VALID_TITLE, VALID_DESCRIPTION);

        given().contentType(ContentType.JSON).cookie("token", this.token).body(input)
                .when().post(PLAYLIST_PATH)
                .then().statusCode(CREATED.value());

        given()
                .contentType(ContentType.JSON)
                .cookie("token", this.token)
                .body(input)
        .when()
                .post(PLAYLIST_PATH)
        .then()
                .statusCode(CONFLICT.value())
                .body("detail", equalTo("There is already a playlist created with this title."));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("invalidTitleProvider")
    void shouldReturnBadRequestWhenTitleIsInvalid(final String invalidTitle) {
        final var input = new CreatePlaylistInput(invalidTitle, VALID_DESCRIPTION);

        given()
                .contentType(ContentType.JSON)
                .cookie("token", this.token)
                .body(input)
        .when()
                .post(PLAYLIST_PATH)
        .then()
                .statusCode(BAD_REQUEST.value())
                .body("detail", equalTo("Error creating a Playlist"));
    }

    @ParameterizedTest
    @MethodSource("invalidDescriptionProvider")
    void shouldReturnBadRequestWhenDescriptionIsInvalid(final String invalidDescription) {
        final var input = new CreatePlaylistInput(VALID_TITLE, invalidDescription);

        given()
                .contentType(ContentType.JSON)
                .cookie("token", this.token)
                .body(input)
        .when()
                .post(PLAYLIST_PATH)
        .then()
                .statusCode(BAD_REQUEST.value())
                .body("detail", equalTo("Error creating a Playlist"));
    }

    @Test
    void shouldGetAllSuccessfully() {
        final var input = new CreatePlaylistInput(VALID_TITLE, VALID_DESCRIPTION);

        given().contentType(ContentType.JSON).cookie("token", this.token).body(input)
                .when().post(PLAYLIST_PATH)
                .then().statusCode(CREATED.value());

        given()
                .contentType(ContentType.JSON)
                .cookie("token", this.token)
                .body(input)
        .when()
                .get(PLAYLIST_PATH)
        .then()
                .statusCode(OK.value())
                .body(is(notNullValue()));
    }

    @Test
    void shouldGetByIdSuccessfully() {
        final var input = new CreatePlaylistInput(VALID_TITLE, VALID_DESCRIPTION);

        final var output =
            given().contentType(ContentType.JSON).cookie("token", this.token).body(input)
                .when().post(PLAYLIST_PATH)
                .then().statusCode(CREATED.value()).extract().body().as(CreatePlaylistOutput.class);

        given()
                .cookie("token", this.token)
        .when()
                .get(PLAYLIST_PATH + output.id())
        .then()
                .statusCode(OK.value())
                .body("id", equalTo(output.id()))
                .body("title", equalTo(VALID_TITLE))
                .body("description", equalTo(VALID_DESCRIPTION));
    }

    @Test
    void shouldReturnBadRequestWhenGettingByIdNull() {
        given()
                .cookie("token", this.token)
        .when()
                .get(PLAYLIST_PATH + null)
        .then()
                .statusCode(BAD_REQUEST.value())
                .body("detail", equalTo("Invalid UUID string: null"));
    }

    @Test
    void shouldReturnNotFoundWhenPlaylistDoesNotExists() {
        given()
                .cookie("token", this.token)
        .when()
                .get(PLAYLIST_PATH + UUID.randomUUID())
        .then()
                .statusCode(NOT_FOUND.value())
                .body("detail", equalTo("There is no playlist with the given id."));
    }

    @Test
    void shouldSearchSuccessfully() {
        final var input = new CreatePlaylistInput(VALID_TITLE, VALID_DESCRIPTION);

        given().contentType(ContentType.JSON).cookie("token", this.token).body(input)
                .when().post(PLAYLIST_PATH)
                .then().statusCode(CREATED.value());

        given()
                .cookie("token", this.token)
                .param("title", VALID_TITLE)
        .when()
                .get(PLAYLIST_PATH + "search")
        .then()
                .statusCode(OK.value())
                .body(is(notNullValue()));
    }

    @Test
    void shouldUpdateSuccessfully() {
        final var createInput = new CreatePlaylistInput(VALID_TITLE, VALID_DESCRIPTION);

        final var output =
            given().contentType(ContentType.JSON).cookie("token", this.token).body(createInput)
                .when().post(PLAYLIST_PATH)
                .then().statusCode(CREATED.value()).extract().body().as(CreatePlaylistOutput.class);

        final String validTitle = "Playlist Title";
        final var input = new UpdatePlaylistInput(validTitle, null);
        given()
                .contentType(ContentType.JSON)
                .cookie("token", this.token)
                .body(input)
        .when()
                .put(PLAYLIST_PATH + output.id())
        .then()
                .statusCode(OK.value())
                .body("id", equalTo(output.id()))
                .body("title", equalTo(validTitle))
                .body("description", equalTo(VALID_DESCRIPTION));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingPlaylistThatDoesNotExists() {
        final var input = new UpdatePlaylistInput(VALID_TITLE, VALID_DESCRIPTION);

        given()
                .contentType(ContentType.JSON)
                .cookie("token", this.token)
                .body(input)
        .when()
                .put(PLAYLIST_PATH + UUID.randomUUID())
        .then()
                .statusCode(NOT_FOUND.value())
                .body("detail", equalTo("There is no playlist with the given id."));
    }

    @Test
    void shouldReturnConflictWhenUpdatingPlaylistWithTitleThatAlreadyExists() {
        final var aCreateInput = new CreatePlaylistInput(VALID_TITLE, VALID_DESCRIPTION);
        final var output =
                given().contentType(ContentType.JSON).cookie("token", this.token).body(aCreateInput)
                        .when().post(PLAYLIST_PATH)
                        .then().statusCode(CREATED.value()).extract().body().as(CreatePlaylistOutput.class);

        final String validTitle = "Playlist Title";
        final var otherCreateInput = new CreatePlaylistInput(validTitle, VALID_DESCRIPTION);
        given().contentType(ContentType.JSON).cookie("token", this.token).body(otherCreateInput)
                .when().post(PLAYLIST_PATH)
                .then().statusCode(CREATED.value());

        final var input = new UpdatePlaylistInput(validTitle, null);
        given()
                .contentType(ContentType.JSON)
                .cookie("token", this.token)
                .body(input)
        .when()
                .put(PLAYLIST_PATH + output.id())
        .then()
                .statusCode(CONFLICT.value())
                .body("detail", equalTo("There is already a playlist created with this title."));
    }

    @Test
    void shouldDeleteSuccessfully() {
        final var createInput = new CreatePlaylistInput(VALID_TITLE, VALID_DESCRIPTION);
        final var output =
                given().contentType(ContentType.JSON).cookie("token", this.token).body(createInput)
                        .when().post(PLAYLIST_PATH)
                        .then().statusCode(CREATED.value()).extract().body().as(CreatePlaylistOutput.class);

        given()
                .cookie("token", this.token)
        .when()
                .delete(PLAYLIST_PATH + output.id())
        .then()
                .statusCode(NO_CONTENT.value());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingPlaylistThatDoesNotExists() {
        given()
                .cookie("token", this.token)
        .when()
                .delete(PLAYLIST_PATH + UUID.randomUUID())
        .then()
                .statusCode(NOT_FOUND.value())
                .body("detail", equalTo("There is no playlist with the given id."));
    }

    @Test
    void shouldAddTitleToPlaylistSuccessfully() {
        final var createInput = new CreatePlaylistInput(VALID_TITLE, VALID_DESCRIPTION);
        final var output =
                given().contentType(ContentType.JSON).cookie("token", this.token).body(createInput)
                        .when().post(PLAYLIST_PATH)
                        .then().statusCode(CREATED.value()).extract().body().as(CreatePlaylistOutput.class);

        final var input = new AddTitleToPlaylistInput(1L, "TV");
        given()
                .contentType(ContentType.JSON)
                .cookie("token", this.token)
                .body(input)
        .when()
                .post(PLAYLIST_PATH + output.id() + "/items/")
        .then()
                .statusCode(NO_CONTENT.value());
    }

    @Test
    void shouldReturnNotFoundWhenAddingTitleToAPlaylistThatDoesNotExists() {
        final var input = new AddTitleToPlaylistInput(1L, "TV");
        given()
                .contentType(ContentType.JSON)
                .cookie("token", this.token)
                .body(input)
        .when()
                .post(PLAYLIST_PATH + UUID.randomUUID() + "/items/")
        .then()
                .statusCode(NOT_FOUND.value())
                .body("detail", equalTo("There is no playlist with the given id."));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "a"})
    void shouldReturnBadRequestWhenAddingToAPlaylistWithInvalidId(final String invalidId) {
        final var input = new AddTitleToPlaylistInput(1L, "TV");
        given()
                .contentType(ContentType.JSON)
                .cookie("token", this.token)
                .body(input)
        .when()
                .post(PLAYLIST_PATH + invalidId + "/items/")
        .then()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void shouldDeleteFromPlaylistSuccessfully() {
        final var createInput = new CreatePlaylistInput(VALID_TITLE, VALID_DESCRIPTION);
        final var output =
                given().contentType(ContentType.JSON).cookie("token", this.token).body(createInput)
                        .when().post(PLAYLIST_PATH)
                        .then().statusCode(CREATED.value()).extract().body().as(CreatePlaylistOutput.class);

        final var addInput = new AddTitleToPlaylistInput(1L, "TV");
        given().contentType(ContentType.JSON).cookie("token", this.token).body(addInput)
                .when().post(PLAYLIST_PATH + output.id() + "/items/")
                .then().statusCode(NO_CONTENT.value());

        given()
                .cookie("token", this.token)
        .when()
                .delete(PLAYLIST_PATH + output.id() + "/items/" + 1L)
        .then()
                .statusCode(NO_CONTENT.value());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingFromPlaylistThatDoesNotExists() {
        given()
                .cookie("token", this.token)
        .when()
                .delete(PLAYLIST_PATH + UUID.randomUUID() + "/items/" + 1L)
        .then()
                .statusCode(NOT_FOUND.value())
                .body("detail", equalTo("There is no playlist with the given id."));
    }

    @Test
    void shouldReturnNotFoundWhenDeletingTitleThatDoesNotExists() {
        final var createInput = new CreatePlaylistInput(VALID_TITLE, VALID_DESCRIPTION);
        final var output =
                given().contentType(ContentType.JSON).cookie("token", this.token).body(createInput)
                        .when().post(PLAYLIST_PATH)
                        .then().statusCode(CREATED.value()).extract().body().as(CreatePlaylistOutput.class);

        given()
                .cookie("token", this.token)
        .when()
                .delete(PLAYLIST_PATH + output.id() + "/items/" + 1L)
        .then()
                .statusCode(NOT_FOUND.value())
                .body("detail", equalTo("This title is not in the playlist."));
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "  ", "\t"})
    void shouldReturnBadRequestWhenDeletingWithInvalidPlaylistId(final String invalidId) {
        given()
                .cookie("token", this.token)
        .when()
                .delete(PLAYLIST_PATH + invalidId + "/items/" + 1L)
        .then()
                .statusCode(BAD_REQUEST.value())
                .body("detail", equalTo("Id cannot be null or empty"));
    }

    @Test
    void shouldGetTitlesFromPlaylistSuccessfully() {
        final var createInput = new CreatePlaylistInput(VALID_TITLE, VALID_DESCRIPTION);
        final var output =
                given().contentType(ContentType.JSON).cookie("token", this.token).body(createInput)
                        .when().post(PLAYLIST_PATH)
                        .then().statusCode(CREATED.value()).extract().body().as(CreatePlaylistOutput.class);

        final var addInput = new AddTitleToPlaylistInput(1L, "TV");
        given().contentType(ContentType.JSON).cookie("token", this.token).body(addInput)
                .when().post(PLAYLIST_PATH + output.id() + "/items/")
                .then().statusCode(NO_CONTENT.value());

        given()
                .cookie("token", this.token)
                .param("page", 1)
        .when()
                .get(PLAYLIST_PATH + output.id() + "/items/")
        .then()
                .statusCode(OK.value())
                .body("titles", is(notNullValue()))
                .body("metadata", is(notNullValue()));
    }

    @Test
    void shouldReturnNotFoundWhenGettingTitleFromAPlaylistThatDoesNotExists() {
        given()
                .cookie("token", this.token)
                .param("page", 1)
        .when()
                .get(PLAYLIST_PATH + UUID.randomUUID() + "/items/")
        .then()
                .statusCode(NOT_FOUND.value())
                .body("detail", equalTo("There is no playlist with the given id."));
    }
}
