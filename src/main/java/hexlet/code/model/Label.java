package hexlet.code.model;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "labels")
@NoArgsConstructor
public class Label extends ModifiedBaseModel {
    public Label(String name) {
        super(name);
    }
}
