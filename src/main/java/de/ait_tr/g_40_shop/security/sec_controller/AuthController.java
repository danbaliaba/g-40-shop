package de.ait_tr.g_40_shop.security.sec_controller;

import de.ait_tr.g_40_shop.domain.entity.User;
import de.ait_tr.g_40_shop.security.sec_dto.RefreshRequestDto;
import de.ait_tr.g_40_shop.security.sec_dto.TokenResponseDto;
import de.ait_tr.g_40_shop.security.sec_service.AuthService;
import jakarta.security.auth.message.AuthException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public TokenResponseDto login(@RequestBody User user) {
        try {
            return service.login(user);
        } catch (AuthException e) {
            return new TokenResponseDto(null, null);
        }
    }

    @PostMapping("/refresh")
    public TokenResponseDto getNewAccessToken(@RequestBody RefreshRequestDto request) {
        return service.getNewAccessToken(request.getRefreshToken());
    }
}
