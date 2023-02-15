package hexlet.code.app.controller;

import hexlet.code.app.domain.DTO.LabelDTO;
import hexlet.code.app.domain.model.Label;
import hexlet.code.app.service.LabelServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import static hexlet.code.app.utils.Description.DELETE;
import static hexlet.code.app.utils.Description.GET;
import static hexlet.code.app.utils.Description.OBJECT_NOT_FOUND;
import static hexlet.code.app.utils.Description.POST;
import static hexlet.code.app.utils.Description.PUT;
import static hexlet.code.app.utils.Description.SUCCESS;
import static hexlet.code.app.utils.Description.UNAUTHORIZED;
import static hexlet.code.app.utils.Description.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping(path = "${base-url}" + LabelController.LABEL_CONTROLLER_PATH)
@Tag(name = "Метки", description = "Работа с метками")
public class LabelController {
    public static final String LABEL_CONTROLLER_PATH = "/labels";
    private static final String ID = "/{id}";
    @Autowired
    private LabelServiceImpl service;


    @Operation(summary = GET)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = SUCCESS),
        @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content)
    })
    @GetMapping
    public List<Label> getLabels() {
        return service.getLabels();
    }

    @Operation(summary = GET)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = SUCCESS,
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Label.class)) }),
        @ApiResponse(responseCode = "404", description = OBJECT_NOT_FOUND, content = @Content),
        @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content)
    })
    @GetMapping(path = ID)
    public Label getLabel(@PathVariable long id) {
        return service.getLabel(id);
    }

    @Operation(summary = POST)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = SUCCESS,
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Label.class)) }),
        @ApiResponse(responseCode = "422", description = UNPROCESSABLE_ENTITY, content = @Content),
        @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content)
    })
    @PostMapping
    public Label createLabel(@RequestBody @Valid LabelDTO labelDTO) {
        return service.saveLabel(labelDTO);
    }

    @Operation(summary = PUT)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = SUCCESS,
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Label.class)) }),
        @ApiResponse(responseCode = "422", description = UNPROCESSABLE_ENTITY, content = @Content),
        @ApiResponse(responseCode = "404", description = OBJECT_NOT_FOUND, content = @Content),
        @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content)
    })
    @PutMapping(path = ID)
    public Label updateLabel(@PathVariable long id, @RequestBody @Valid LabelDTO labelDTO) {
        return service.updateLabel(labelDTO, id);
    }

    @Operation(summary = DELETE)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = SUCCESS, content = @Content),
        @ApiResponse(responseCode = "422", description = UNPROCESSABLE_ENTITY, content = @Content),
        @ApiResponse(responseCode = "404", description = OBJECT_NOT_FOUND, content = @Content),
        @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content)
    })
    @DeleteMapping(path = ID)
    public void deleteLabel(@PathVariable long id) {
        service.deleteLabel(id);
    }
}
