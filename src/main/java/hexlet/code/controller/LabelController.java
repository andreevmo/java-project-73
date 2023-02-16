package hexlet.code.controller;

import hexlet.code.domain.DTO.LabelDTO;
import hexlet.code.domain.model.Label;
import hexlet.code.service.LabelServiceImpl;
import hexlet.code.utils.Description;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@RestController
@RequestMapping(path = "${base-url}" + LabelController.LABEL_CONTROLLER_PATH)
@Tag(name = "Метки", description = "Работа с метками")
public class LabelController {
    public static final String LABEL_CONTROLLER_PATH = "/labels";
    private static final String ID = "/{id}";
    @Autowired
    private LabelServiceImpl service;


    @Operation(summary = Description.GET)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = Description.SUCCESS),
        @ApiResponse(responseCode = "401", description = Description.UNAUTHORIZED, content = @Content)
    })
    @GetMapping()
    public List<Label> getLabels() {
        return service.getLabels();
    }

    @Operation(summary = Description.GET)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = Description.SUCCESS,
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Label.class)) }),
        @ApiResponse(responseCode = "404", description = Description.OBJECT_NOT_FOUND, content = @Content),
        @ApiResponse(responseCode = "401", description = Description.UNAUTHORIZED, content = @Content)
    })
    @GetMapping(path = ID)
    public Label getLabel(@PathVariable long id) {
        return service.getLabel(id);
    }

    @Operation(summary = Description.POST)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = Description.SUCCESS,
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Label.class)) }),
        @ApiResponse(responseCode = "422", description = Description.UNPROCESSABLE_ENTITY, content = @Content),
        @ApiResponse(responseCode = "401", description = Description.UNAUTHORIZED, content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Label createLabel(@RequestBody @Valid LabelDTO labelDTO) {
        return service.saveLabel(labelDTO);
    }

    @Operation(summary = Description.PUT)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = Description.SUCCESS,
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Label.class)) }),
        @ApiResponse(responseCode = "422", description = Description.UNPROCESSABLE_ENTITY, content = @Content),
        @ApiResponse(responseCode = "404", description = Description.OBJECT_NOT_FOUND, content = @Content),
        @ApiResponse(responseCode = "401", description = Description.UNAUTHORIZED, content = @Content)
    })
    @PutMapping(path = ID)
    public Label updateLabel(@PathVariable long id, @RequestBody @Valid LabelDTO labelDTO) {
        return service.updateLabel(labelDTO, id);
    }

    @Operation(summary = Description.DELETE)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = Description.SUCCESS, content = @Content),
        @ApiResponse(responseCode = "422", description = Description.UNPROCESSABLE_ENTITY, content = @Content),
        @ApiResponse(responseCode = "404", description = Description.OBJECT_NOT_FOUND, content = @Content),
        @ApiResponse(responseCode = "401", description = Description.UNAUTHORIZED, content = @Content)
    })
    @DeleteMapping(path = ID)
    public void deleteLabel(@PathVariable long id) {
        service.deleteLabel(id);
    }
}
