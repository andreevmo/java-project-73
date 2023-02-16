package hexlet.code.service;

import hexlet.code.domain.DTO.LabelDTO;
import hexlet.code.domain.model.Label;

import java.util.List;

public interface LabelService {

    Label getLabel(long id);
    List<Label> getLabels();
    Label saveLabel(LabelDTO labelDTO);
    Label updateLabel(LabelDTO labelDTO, long id);
    void deleteLabel(long id);
}
