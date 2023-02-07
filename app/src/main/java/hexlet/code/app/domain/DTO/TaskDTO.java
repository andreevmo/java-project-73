package hexlet.code.app.domain.DTO;

import hexlet.code.app.domain.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    @NotBlank
    @Size(min = 1)
    private String name;
    private String description;
    @NotNull
    private Long taskStatusId;
    private User author;
    private Long executorId;
    private Date createdAt;

    public TaskDTO(Long id, String name, String description, long taskStatusId, User author, long executorId,
                   Date created) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.taskStatusId = taskStatusId;
        this.author = author;
        this.executorId = executorId;
        this.createdAt = created;
    }

}
