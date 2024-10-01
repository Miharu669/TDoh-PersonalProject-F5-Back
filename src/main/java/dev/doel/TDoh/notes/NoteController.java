package dev.doel.TDoh.notes;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import dev.doel.TDoh.users.User;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("${api-endpoint}/notes")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<List<NoteDTO>> getUserNotes(Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        List<NoteDTO> notes = noteService.getNotesByUserId(user.getId());
        return ResponseEntity.ok(notes);
    }

    @PostMapping
    public ResponseEntity<NoteDTO> createNote(Principal connectedUser,@RequestBody NoteDTO noteDTO) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        NoteDTO createdNote = noteService.createNote(user.getId(), noteDTO);
        return ResponseEntity.ok(createdNote);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> updateNote(Principal connectedUser, @RequestBody NoteDTO noteDTO) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        NoteDTO updatedNote = noteService.updateNoteById(user.getId(), noteDTO);

        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNoteById(Principal connectedUser, @PathVariable Long id) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        noteService.deleteNoteById(id, user.getId());
        return ResponseEntity.noContent().build();
    }

}
