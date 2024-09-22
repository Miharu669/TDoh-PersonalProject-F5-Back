package dev.doel.TDoh.minitask;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MiniTaskDTO {

    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 100, message = "Title must be up to 100 characters")
    private String title;

    @Size(max = 300, message = "Description must be up to 300 characters")
    private String description;

    @NotNull(message = "Completion status must be specified")
    private boolean isDone;

    @NotNull(message = "SubTask ID must be specified")
    private Long subTaskId;
}
