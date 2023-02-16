package hexlet.code.service;

import hexlet.code.domain.DTO.LabelDTO;
import hexlet.code.domain.model.Label;
import hexlet.code.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LabelServiceImpl implements LabelService {

    @Autowired
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
    public Label updateLabel(LabelDTO labelDTO, long id) {
        Label labelFromDB = labelRepository.findById(id).orElseThrow();
        labelFromDB.setName(labelDTO.getName());
        return labelRepository.save(labelFromDB);
    }

    @Override
    public void deleteLabel(long id) {
        if (!labelRepository.existsById(id)) {
            throw new NoSuchElementException();
        }
        labelRepository.deleteById(id);
    }

    private Label createLabel(LabelDTO labelDTO) {
        return new Label(labelDTO.getName());
    }
}
