package dev.doel.TDoh.task;

import java.util.List;

import dev.doel.TDoh.subtask.SubTaskDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {

    private long id;

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    private String title;

    @Size(max = 255, message = "Description can have up to 255 characters")
    private String description;

    private boolean isDone;

    private List<SubTaskDTO> subTasks;

}