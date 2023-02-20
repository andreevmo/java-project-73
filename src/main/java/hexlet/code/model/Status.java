package hexlet.code.model;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "statuses")
@NoArgsConstructor
public class Status extends ModifiedBaseModel {
    public Status(String name) {
        super(name);
    }
}
