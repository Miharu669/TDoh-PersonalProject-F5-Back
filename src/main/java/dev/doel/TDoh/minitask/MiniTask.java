package dev.doel.TDoh.minitask;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "minitask")

public class MiniTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "minitask_id")
    private long id;

    @Column
    private String title;
    private String description;

    @Builder.Default
    private boolean isDone = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subtask_id", nullable = false)
    private MiniTask miniTask;

}
