package hexlet.code.service;

import hexlet.code.DTO.StatusDTO;
import hexlet.code.model.Status;

import java.util.List;

public interface StatusService {

    Status saveStatus(StatusDTO status);
    Status updateStatus(StatusDTO status, Long id);
    Status getStatus(Long id);
    List<Status> getAll();
    void deleteStatus(Long id);
}
