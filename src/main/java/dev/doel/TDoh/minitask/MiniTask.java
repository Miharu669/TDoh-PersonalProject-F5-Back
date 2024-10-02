// package dev.doel.TDoh.minitask;

// import dev.doel.TDoh.subtask.SubTask;
// import jakarta.persistence.*;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.Size;
// import lombok.*;

// @Entity
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// @Table(name = "minitasks")
// public class MiniTask {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     @Column(name = "minitask_id")
//     private Long id;

//     @NotBlank(message = "Title cannot be blank")
//     @Size(max = 100, message = "Title can have at most 100 characters")
//     @Column(nullable = false, length = 100)
//     private String title;

//     @Size(max = 300, message = "Description can have at most 300 characters")
//     @Column(length = 300)
//     private String description;

//     @Builder.Default
//     @Column(nullable = false)
//     private boolean isDone = false;

//     @ManyToOne(fetch = FetchType.EAGER) 
//     @JoinColumn(name = "subtask_id", nullable = false)
//     private SubTask subTask;
    
// }
