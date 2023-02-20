package hexlet.code.controller;

import hexlet.code.DTO.StatusDTO;
import hexlet.code.model.Status;
import hexlet.code.service.StatusServiceImpl;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static hexlet.code.controller.StatusController.STATUS_CONTROLLER_PATH;

@RestController
@RequestMapping(path = "${base-url}" + STATUS_CONTROLLER_PATH)
@Hidden
public class StatusController {

    @Autowired
    private StatusServiceImpl service;

    public static final String STATUS_CONTROLLER_PATH = "/statuses";
    public static final String ID = "{id}";

    @GetMapping(path = ID)
    public Status getStatus(@PathVariable Long id) {
        return service.getStatus(id);
    }

    @GetMapping
    public List<Status> getStatuses() {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Status createStatus(@RequestBody @Valid StatusDTO statusDTO) {
        return service.saveStatus(statusDTO);
    }

    @PutMapping(path = ID)
    public Status updateStatus(@RequestBody @Valid StatusDTO statusDTO, @PathVariable Long id) {
        return service.updateStatus(statusDTO, id);
    }

    @DeleteMapping(path = ID)
    public void deleteStatus(@PathVariable Long id) {
        service.deleteStatus(id);
    }
}
