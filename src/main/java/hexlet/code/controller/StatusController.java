package hexlet.code.controller;

import hexlet.code.DTO.StatusDTO;
import hexlet.code.model.Status;
import hexlet.code.service.StatusService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class StatusController {

    private StatusService statusService;

    public static final String STATUS_CONTROLLER_PATH = "/statuses";
    public static final String ID = "{id}";

    @GetMapping(path = ID)
    public Status getStatus(@PathVariable Long id) {
        return statusService.getStatus(id);
    }

    @GetMapping
    public List<Status> getStatuses() {
        return statusService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Status createStatus(@RequestBody @Valid StatusDTO statusDTO) {
        return statusService.saveStatus(statusDTO);
    }

    @PutMapping(path = ID)
    public Status updateStatus(@RequestBody @Valid StatusDTO statusDTO, @PathVariable Long id) {
        return statusService.updateStatus(statusDTO, id);
    }

    @DeleteMapping(path = ID)
    public void deleteStatus(@PathVariable Long id) {
        statusService.deleteStatus(id);
    }
}
