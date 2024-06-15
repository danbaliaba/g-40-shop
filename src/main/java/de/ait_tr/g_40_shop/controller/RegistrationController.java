package de.ait_tr.g_40_shop.controller;


import de.ait_tr.g_40_shop.domain.entity.User;
import de.ait_tr.g_40_shop.exception_handling.Response;
import de.ait_tr.g_40_shop.service.Interfaces.ConfirmationService;
import de.ait_tr.g_40_shop.service.Interfaces.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final UserService service;
    private final ConfirmationService confirmationService;

    public RegistrationController(UserService service, ConfirmationService confirmationService) {
        this.service = service;
        this.confirmationService = confirmationService;
    }

    @PostMapping
    public Response register(@RequestBody User user) {

        service.register(user);

        return new Response("Registration complete. Please check your email");
    }

    @GetMapping
    public Response activation(@RequestParam String code) {

        if (code != null) {
            confirmationService.confirmAccount(code);
        }

        return new Response("Account is now active");

    }
}
