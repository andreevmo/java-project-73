package hexlet.code.app.domain.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class LabelDTO {

    private long id;
    @NotBlank
    @Size(min = 1)
    private String name;
    private Date createdAt;
}
