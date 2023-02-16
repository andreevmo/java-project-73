package hexlet.code.service;

import hexlet.code.domain.DTO.StatusDTO;
import hexlet.code.domain.model.Status;

import java.util.List;

public interface StatusService {

    Status saveStatus(StatusDTO status);
    Status updateStatus(StatusDTO status, Long id);
    Status getStatus(Long id);
    List<Status> getAll();
    void deleteStatus(Long id);
}
