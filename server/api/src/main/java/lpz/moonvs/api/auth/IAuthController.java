package lpz.moonvs.api.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lpz.moonvs.api.auth.input.LoginInput;
import lpz.moonvs.api.auth.input.RegisterInput;
import lpz.moonvs.application.auth.output.LoginOutput;
import lpz.moonvs.application.auth.output.RegisterOutput;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Authentication")
public interface IAuthController {

    @Operation(
            summary = "Register a new user",
            description = "Validates the credentials and register a new user with the given credentials",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User registered successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = RegisterOutput.class),
                                    examples = {@ExampleObject(name = "User registered", externalValue = "/openapi/auth/register.success.json")}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid credentials",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {@ExampleObject(name = "Invalid Credentials", externalValue = "/openapi/auth/register.bad-request.json")}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "User already exists",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {@ExampleObject(name = "Existing User", externalValue = "/openapi/auth/register.conflict.json")}
                            )
                    )
            }
    )
    @PostMapping("/register")
    ResponseEntity<RegisterOutput> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RegisterInput.class),
                            examples = {@ExampleObject(name = "New User", externalValue = "/openapi/auth/register.request.json")}
                    )
            )
            @RequestBody final RegisterInput input);


    @Operation(
            summary = "Authenticates an existing user",
            description = "Validates the given credentials, and if they are correct, authenticates the user, returning the JWT in the cookie.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User authenticated successfully!",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = LoginOutput.class),
                                    examples = {@ExampleObject(name = "User authenticated", externalValue = "/openapi/auth/login.success.json")}
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User does not exist",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                    schema = @Schema(implementation = ProblemDetail.class),
                                    examples = {@ExampleObject(name = "User not found", externalValue = "/openapi/auth/login.not-found.json")}
                            )
                    )
            }
    )
    @PostMapping("/login")
    ResponseEntity<LoginOutput> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LoginInput.class),
                            examples = {@ExampleObject(name = "Existing user", externalValue = "/openapi/auth/login.request.json")}
                    )
            )
            @RequestBody final LoginInput input);
}
