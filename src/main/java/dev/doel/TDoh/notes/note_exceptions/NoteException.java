package dev.doel.TDoh.notes.note_exceptions;

public class NoteException extends RuntimeException {
    public NoteException(String message) {
        super(message);
    }

    public NoteException(String message, Throwable cause) {
        super(message, cause);
    }
}
