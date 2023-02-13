package hexlet.code.app.domain.DTO;

import hexlet.code.app.domain.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class TaskDTO extends BaseDTO {

    @NotBlank
    @Size(min = 1)
    private String name;
    private String description;
    @NotNull
    private Long taskStatusId;
    private Long[] labelIds;
    private User author;
    private Long executorId;
}
