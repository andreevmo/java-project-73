package hexlet.code.DTO;

import lombok.Data;

import java.util.Date;

@Data
public abstract class BaseDTO {

    private long id;
    private Date createdAt;
}
