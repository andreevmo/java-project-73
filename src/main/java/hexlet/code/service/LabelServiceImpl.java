package hexlet.code.service;

import hexlet.code.DTO.LabelDTO;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private LabelRepository labelRepository;
    @Override
    public Label getLabel(long id) {
        return labelRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Label> getLabels() {
        return (List<Label>) labelRepository.findAll();
    }

    @Override
    public Label saveLabel(LabelDTO labelDTO) {
        Label label = createLabel(labelDTO);
        return labelRepository.save(label);
    }

    @Override
    @Transactional
    public Label updateLabel(LabelDTO labelDTO, long id) {
        Label labelFromDB = labelRepository.findById(id).orElseThrow();
        labelFromDB.setName(labelDTO.getName());
        return labelFromDB;
    }

    @Override
    public void deleteLabel(long id) {
        labelRepository.deleteById(id);
    }

    private Label createLabel(LabelDTO labelDTO) {
        return new Label(labelDTO.getName());
    }
}
