package dev.doel.TDoh.subtask;

import lombok.*;

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
}
