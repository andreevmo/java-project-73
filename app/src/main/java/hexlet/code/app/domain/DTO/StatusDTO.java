package hexlet.code.app.domain.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusDTO {

    private Long id;
    private String name;
    private Date createdAt;
}
