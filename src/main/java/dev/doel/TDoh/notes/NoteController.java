package dev.doel.TDoh.notes;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api-endpoint}/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<List<NoteDTO>> getUserNotes(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername()); 
        List<NoteDTO> notes = noteService.getNotesByUserId(userId);
        return ResponseEntity.ok(notes);
    }

    @PostMapping
    public ResponseEntity<NoteDTO> createNote(@AuthenticationPrincipal UserDetails userDetails, @RequestBody NoteDTO noteDTO) {
        Long userId = Long.parseLong(userDetails.getUsername());
        NoteDTO createdNote = noteService.createNote(userId, noteDTO);
        return ResponseEntity.ok(createdNote);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable Long id, @RequestBody NoteDTO noteDTO) {
        NoteDTO updatedNote = noteService.updateNoteById(id, noteDTO);

        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNoteById(@PathVariable Long id) {
        noteService.deleteNoteById(id);
        return ResponseEntity.noContent().build();
    }

}
