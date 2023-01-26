package hexlet.code.app.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${base-url}")
public final class WelcomeController {

    @GetMapping(path = "/welcome")
    public String greet() {
        return "Welcome to Spring";
    }
}
