package hexlet.code.app.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.NonNull;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToOne(fetch = FetchType.EAGER)
    private Status taskStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    private User author;
    @ManyToOne(fetch = FetchType.EAGER)
    private User executor;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "task_label",
            joinColumns = {@JoinColumn(name = "task_id")},
            inverseJoinColumns = {@JoinColumn(name = "label_id")})
    private List<Label> labels;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Task(String name, String description, Status taskStatus, @NonNull User author, User executor,
                List<Label> labels) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.author = author;
        this.executor = executor;
        this.labels = labels;
    }
}
