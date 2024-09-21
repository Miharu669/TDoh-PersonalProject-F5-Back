package dev.doel.TDoh.task;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {

    private long id;

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    private String title;

    @Size(max = 255, message = "Description can have up to 255 characters")
    private String description;

    private boolean isDone;

    private List<SubTaskDTO> subTasks;

}
