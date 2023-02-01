package hexlet.code.app.service;

import hexlet.code.app.domain.DTO.StatusDTO;
import hexlet.code.app.domain.model.Status;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface StatusService {

    StatusDTO saveStatus(Status status, BindingResult bindingResult);

    StatusDTO updateStatus(Status status, Long id, BindingResult bindingResult);

    StatusDTO getStatus(Long id);
    List<StatusDTO> getAll();
    void deleteStatus(Long id);

    StatusDTO getStatusDTO(Status status);

}
