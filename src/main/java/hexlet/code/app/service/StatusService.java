package hexlet.code.app.service;

import hexlet.code.app.domain.DTO.StatusDTO;
import hexlet.code.app.domain.model.Status;

import java.util.List;

public interface StatusService {

    Status saveStatus(StatusDTO status);
    Status updateStatus(StatusDTO status, Long id);
    Status getStatus(Long id);
    List<Status> getAll();
    void deleteStatus(Long id);
}
