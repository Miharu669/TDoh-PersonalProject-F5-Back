package dev.doel.TDoh.subtask;

import java.util.List;

import dev.doel.TDoh.minitask.MiniTask;
import dev.doel.TDoh.task.Task;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "subtasks")
public class SubTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subtask_id")
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title can have at most 100 characters")
    @Column(nullable = false, length = 100)
    private String title;
    
    @Size(max = 300, message = "Description can have at most 300 characters")
    @Column(length = 300)
    private String description;

    @Builder.Default
    private boolean isDone = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @OneToMany(mappedBy = "subTask", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<MiniTask> miniTasks;
    

}
