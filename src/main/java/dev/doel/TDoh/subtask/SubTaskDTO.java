package dev.doel.TDoh.subtask;

import lombok.*;
// import java.util.List;

// import dev.doel.TDoh.minitask.MiniTaskDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubTaskDTO {

    private Long id;
    private String title;
    private String description;
    private boolean isDone;
    private Long taskId;
    // private List<MiniTaskDTO> miniTasks;
}
