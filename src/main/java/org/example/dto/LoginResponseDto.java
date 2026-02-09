package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Response returned after successful login")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class LoginResponseDto {

    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private final String token;

    @Schema(description = "Token type", example = "Bearer")
    private final String type;

    @Schema(description = "Authenticated username", example = "user")
    private final String username;

    @Schema(description = "Roles of the user", example = "[\"ROLE_USER\"]")
    private final List<String> roles;
}