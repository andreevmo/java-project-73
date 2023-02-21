package hexlet.code.service;

import hexlet.code.DTO.StatusDTO;
import hexlet.code.model.Status;
import hexlet.code.repository.StatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class StatusServiceImpl implements StatusService {

    private StatusRepository statusRepository;
    @Override
    public Status saveStatus(StatusDTO statusDTO) {
        Status status = createStatus(statusDTO);
        return statusRepository.save(status);
    }

    @Override
    @Transactional
    public Status updateStatus(StatusDTO statusDTO, Long id) {
        Status status = createStatus(statusDTO);
        Status statusFromDB = statusRepository.findById(id).orElseThrow();
        statusFromDB.setName(status.getName());
        return statusFromDB;
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
        statusRepository.deleteById(id);
    }

    private Status createStatus(StatusDTO statusDTO) {
        return new Status(statusDTO.getName());
    }
}
