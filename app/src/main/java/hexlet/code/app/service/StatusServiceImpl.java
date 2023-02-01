package hexlet.code.app.service;

import hexlet.code.app.domain.DTO.StatusDTO;
import hexlet.code.app.domain.model.Status;
import hexlet.code.app.exception.IncorrectInputException;
import hexlet.code.app.exception.StatusNotFoundException;
import hexlet.code.app.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    private StatusRepository statusRepository;
    @Override
    public StatusDTO saveStatus(Status status, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IncorrectInputException("Input incorrect");
        }
        return getStatusDTO(statusRepository.save(status));
    }

    @Override
    public StatusDTO updateStatus(Status status, Long id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IncorrectInputException("Input incorrect");
        }
        Status statusFromDB = statusRepository.findById(id)
                .orElseThrow(() -> new StatusNotFoundException("Status with such id not found"));
        statusFromDB.setName(status.getName());
        return getStatusDTO(statusRepository.save(status));
    }

    @Override
    public StatusDTO getStatus(Long id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new StatusNotFoundException("Status with such id not found"));
        return getStatusDTO(status);
    }

    @Override
    public List<StatusDTO> getAll() {
        return StreamSupport.stream(statusRepository.findAll().spliterator(), false)
                .map(this::getStatusDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteStatus(Long id) {
        Status statusFromDB = statusRepository.findById(id)
                .orElseThrow(() -> new StatusNotFoundException("Status with such id not found"));
        statusRepository.delete(statusFromDB);
    }

    @Override
    public StatusDTO getStatusDTO(Status status) {
        return new StatusDTO(status.getId(), status.getName(), status.getCreatedAt());
    }
}
