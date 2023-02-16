package hexlet.code.app.controller;


import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping(path = "/welcome")
public final class WelcomeController {

    @GetMapping
    public String greet() {
        return "Welcome to Spring";
    }
}
