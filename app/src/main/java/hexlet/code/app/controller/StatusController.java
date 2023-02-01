package hexlet.code.app.controller;

import hexlet.code.app.domain.DTO.StatusDTO;
import hexlet.code.app.domain.model.Status;
import hexlet.code.app.service.StatusServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "${base-url}" + "/statuses")
public class StatusController {

    @Autowired
    private StatusServiceImpl service;

    @GetMapping(path = "/{id}")
    public StatusDTO getStatus(@PathVariable Long id) {
        return service.getStatus(id);
    }

    @GetMapping(path = "")
    public List<StatusDTO> getStatuses() {
        return service.getAll();
    }

    @PostMapping(path = "")
    public StatusDTO createStatus(@Valid @RequestBody Status status, BindingResult bindingResult) {
        return service.saveStatus(status, bindingResult);
    }

    @PutMapping(path = "/{id}")
    public StatusDTO updateStatus(
            @Valid @RequestBody Status status, @PathVariable Long id, BindingResult bindingResult) {
        return service.updateStatus(status, id, bindingResult);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteStatus(@PathVariable Long id) {
        service.deleteStatus(id);
    }
}
