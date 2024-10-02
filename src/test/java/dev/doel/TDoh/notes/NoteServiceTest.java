package dev.doel.TDoh.notes;

import dev.doel.TDoh.notes.note_exceptions.NoteNotFoundException;
import dev.doel.TDoh.users.User;
import dev.doel.TDoh.users.UserRepository;
import dev.doel.TDoh.users.user_exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NoteService noteService;

    private User user;
    private Note note;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User(); 
        user.setId(1L);

        note = Note.builder()
                .id(1L)
                .title("Test Title")
                .content("Test Content")
                .user(user)
                .createdAt(System.currentTimeMillis())
                .build();
    }

    @Test
    void testGetNotesByUserId() {
        when(noteRepository.findByUserId(user.getId())).thenReturn(Arrays.asList(note));

        List<NoteDTO> notes = noteService.getNotesByUserId(user.getId());

        assertEquals(1, notes.size());
        assertEquals("Test Title", notes.get(0).getTitle());
        verify(noteRepository).findByUserId(user.getId());
    }

    @Test
void testCreateNote() {
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> invocation.getArgument(0)); 

    NoteDTO noteDTO = new NoteDTO(null, "New Note", "New Content", null);
    NoteDTO createdNote = noteService.createNote(user.getId(), noteDTO);

    assertNotNull(createdNote);
    assertEquals("New Note", createdNote.getTitle());
    assertEquals("New Content", createdNote.getContent());
    verify(userRepository).findById(user.getId());
    verify(noteRepository).save(any(Note.class));
}


    @Test
    void testCreateNote_UserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> noteService.createNote(user.getId(), new NoteDTO(1l, "New Note", "New Content", null)));
    }

    @Test
    void testUpdateNoteById() {
        when(noteRepository.findById(note.getId())).thenReturn(Optional.of(note));
        when(noteRepository.save(any(Note.class))).thenReturn(note);

        NoteDTO updatedNote = noteService.updateNoteById(note.getId(), new NoteDTO(1L, "Updated Title", "Updated Content", null));

        assertEquals("Updated Title", updatedNote.getTitle());
        assertEquals("Updated Content", updatedNote.getContent());
        verify(noteRepository).findById(note.getId());
        verify(noteRepository).save(any(Note.class));
    }

    @Test
    void testUpdateNoteById_NoteNotFound() {
        when(noteRepository.findById(note.getId())).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class, () -> noteService.updateNoteById(note.getId(), new NoteDTO(1L, "Updated Title", "Updated Content", null)));
    }

    @Test
    void testDeleteNoteById() {
        when(noteRepository.findById(note.getId())).thenReturn(Optional.of(note));

        noteService.deleteNoteById(note.getId(), user.getId());

        verify(noteRepository).delete(note);
    }

    @Test
    void testDeleteNoteById_NoteNotFound() {
        when(noteRepository.findById(note.getId())).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class, () -> noteService.deleteNoteById(note.getId(), user.getId()));
    }
}
