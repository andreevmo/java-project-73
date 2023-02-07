package hexlet.code.app.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusDTO {

    private Long id;
    @NotBlank
    @Size(min = 1)
    private String name;
    private Date createdAt;
}
