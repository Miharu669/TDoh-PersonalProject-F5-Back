package dev.doel.TDoh.task;

import java.util.ArrayList;
import java.util.List;

import dev.doel.TDoh.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 300)
     private String description;

    private boolean isDone = false;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubTask> subTasks = new ArrayList<>();

     @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}