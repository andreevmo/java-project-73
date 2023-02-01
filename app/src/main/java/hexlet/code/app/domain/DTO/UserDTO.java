package hexlet.code.app.domain.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private Date createdAt;
    @JsonIgnore
    private String password;

}
