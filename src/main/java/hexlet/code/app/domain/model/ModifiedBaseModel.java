package hexlet.code.app.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public abstract class ModifiedBaseModel extends BaseModel {
    private String name;
    public ModifiedBaseModel(String name) {
        this.name = name;
    }
}
