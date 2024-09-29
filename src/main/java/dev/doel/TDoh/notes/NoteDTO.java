package dev.doel.TDoh.notes;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoteDTO {
    private Long id;
    private String title;
    private String content;
    private Long createdAt;
}
