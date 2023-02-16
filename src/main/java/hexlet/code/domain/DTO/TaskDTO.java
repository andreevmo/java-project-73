package hexlet.code.domain.DTO;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Hidden
public class TaskDTO extends BaseDTO {

    @NotBlank
    @Size(min = 1)
    private String name;
    private String description;
    @NotNull
    private Long taskStatusId;
    private Long[] labelIds;
    private Long authorId;
    private Long executorId;
}
