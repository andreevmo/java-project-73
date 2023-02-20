package hexlet.code.service;

import hexlet.code.DTO.LabelDTO;
import hexlet.code.model.Label;

import java.util.List;

public interface LabelService {

    Label getLabel(long id);
    List<Label> getLabels();
    Label saveLabel(LabelDTO labelDTO);
    Label updateLabel(LabelDTO labelDTO, long id);
    void deleteLabel(long id);
}
