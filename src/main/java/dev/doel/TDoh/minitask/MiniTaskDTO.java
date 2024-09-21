package dev.doel.TDoh.minitask;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MiniTaskDTO {

    private long id;
    private String title;
    private String description;
    private boolean isDone;
    private Long subTaskId;

}
