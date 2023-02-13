package hexlet.code.app.service;

import hexlet.code.app.domain.DTO.StatusDTO;
import hexlet.code.app.domain.model.Status;
import hexlet.code.app.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StatusServiceImpl implements StatusService {

    @Autowired
    private StatusRepository statusRepository;
    @Override
    public Status saveStatus(StatusDTO statusDTO) {
        Status status = createStatus(statusDTO);
        return statusRepository.save(status);
    }

    @Override
    public Status updateStatus(StatusDTO statusDTO, Long id) {
        Status status = createStatus(statusDTO);
        Status statusFromDB = statusRepository.findById(id)
                .orElseThrow();
        statusFromDB.setName(status.getName());
        return statusRepository.save(statusFromDB);
    }

    @Override
    public Status getStatus(Long id) {
        return statusRepository.findById(id)
                .orElseThrow();
    }

    @Override
    public List<Status> getAll() {
        return (List<Status>) statusRepository.findAll();
    }

    @Override
    public void deleteStatus(Long id) {
        if (!statusRepository.existsById(id)) {
            throw new NoSuchElementException();
        }
        statusRepository.deleteById(id);
    }

    private Status createStatus(StatusDTO statusDTO) {
        return new Status(statusDTO.getName());
    }
}
