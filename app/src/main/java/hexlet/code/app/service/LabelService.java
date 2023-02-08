package hexlet.code.app.service;

import hexlet.code.app.domain.DTO.LabelDTO;
import hexlet.code.app.domain.model.Label;

import java.util.List;

public interface LabelService {

    Label getLabel(long id);
    List<Label> getLabels();
    Label saveLabel(LabelDTO labelDTO);
    Label updateLabel(LabelDTO labelDTO, long id);
    void deleteLabel(long id);
}
