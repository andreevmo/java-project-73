package hexlet.code.app.controller;


import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@RequestMapping(path = "${base-url}")
public final class WelcomeController {

    @GetMapping(path = "/welcome")
    public String greet() {
        return "Welcome to Spring";
    }
}
