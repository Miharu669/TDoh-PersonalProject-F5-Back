package dev.doel.TDoh.agenda;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDTO {
    
    private Long id; 

    @NotEmpty(message = "Event name cannot be empty")
    private String name;

    @NotNull(message = "Event date cannot be null")
    private LocalDate date;
}

