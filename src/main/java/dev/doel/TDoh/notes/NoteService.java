package dev.doel.TDoh.notes;

import dev.doel.TDoh.users.User;
import dev.doel.TDoh.users.UserRepository;
import dev.doel.TDoh.users.user_exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public List<NoteDTO> getNotesByUserId(Long userId) {
        return noteRepository.findByUserId(userId).stream()
                .map(note -> new NoteDTO(note.getId(), note.getTitle(), note.getContent(), note.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public NoteDTO createNote(Long userId, NoteDTO noteDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Note newNote = Note.builder()
                .title(noteDTO.getTitle())
                .content(noteDTO.getContent())
                .user(user)
                .createdAt(System.currentTimeMillis())
                .build();

        Note savedNote = noteRepository.save(newNote);

        return new NoteDTO(savedNote.getId(), savedNote.getTitle(), savedNote.getContent(), savedNote.getCreatedAt());
    }
}
