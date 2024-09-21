package dev.doel.TDoh.subtask;

import dev.doel.TDoh.task.Task;
import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String title;

    private String description;

    @Builder.Default
    private boolean isDone = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

   
}
