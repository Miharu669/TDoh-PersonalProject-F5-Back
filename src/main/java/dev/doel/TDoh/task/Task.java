package dev.doel.TDoh.task;

import java.util.List;

import dev.doel.TDoh.subtask.SubTask;
import dev.doel.TDoh.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tasks")

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title can have at most 100 characters")
    @Column(nullable = false, length = 100)
    private String title;

    @Size(max = 300, message = "Description can have at most 300 characters")
    private String description;

    @Builder.Default
    @Column(nullable = false)

    private boolean isDone = false;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SubTask> subTasks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}