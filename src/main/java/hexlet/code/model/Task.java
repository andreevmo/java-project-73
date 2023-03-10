package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task extends BaseModel {

    private String name;
    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    private Status taskStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @NonNull
    private User author;
    @ManyToOne(fetch = FetchType.EAGER)
    private User executor;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "task_label",
            joinColumns = {@JoinColumn(name = "task_id")},
            inverseJoinColumns = {@JoinColumn(name = "label_id")})
    private Set<Label> labels;

}
