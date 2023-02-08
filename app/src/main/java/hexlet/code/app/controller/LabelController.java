package hexlet.code.app.controller;

import hexlet.code.app.domain.DTO.LabelDTO;
import hexlet.code.app.domain.model.Label;
import hexlet.code.app.service.LabelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(path =  "${base-url}" + LabelController.LABEL_CONTROLLER_PATH)
public class LabelController {
    public static final String LABEL_CONTROLLER_PATH = "/labels";
    private static final String ID = "/{id}";
    @Autowired
    private LabelServiceImpl service;

    @GetMapping
    public List<Label> getLabels() {
        return service.getLabels();
    }

    @GetMapping(path = ID)
    public Label getLabel(@PathVariable long id) {
        return service.getLabel(id);
    }

    @PostMapping
    public Label createLabel(@RequestBody @Valid LabelDTO labelDTO) {
        return service.saveLabel(labelDTO);
    }

    @PutMapping(path = ID)
    public Label updateLabel(@PathVariable long id, @RequestBody @Valid LabelDTO labelDTO) {
        return service.updateLabel(labelDTO, id);
    }

    @DeleteMapping(path = ID)
    public void deleteLabel(@PathVariable long id) {
        service.deleteLabel(id);
    }
}
