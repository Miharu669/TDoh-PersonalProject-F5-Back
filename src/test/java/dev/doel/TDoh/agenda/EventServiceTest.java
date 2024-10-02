package dev.doel.TDoh.agenda;

import dev.doel.TDoh.agenda.agenda_exceptions.EventNotFoundException;
import dev.doel.TDoh.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L); 
    }

    @Test
    void testCreateEvent() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setName("Test Event");
        eventDTO.setDate(LocalDate.now());

        Event savedEvent = new Event();
        savedEvent.setId(1L);
        savedEvent.setName(eventDTO.getName());
        savedEvent.setDate(eventDTO.getDate());
        savedEvent.setUser(user);

        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);

        EventDTO createdEvent = eventService.createEvent(eventDTO, user.getId());

        assertNotNull(createdEvent);
        assertEquals("Test Event", createdEvent.getName());
        assertEquals(savedEvent.getId(), createdEvent.getId());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void testGetEventsByDate() {
        LocalDate date = LocalDate.now();
        Event event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setDate(date);
        event.setUser(user);

        when(eventRepository.findByDateAndUserId(date, user.getId())).thenReturn(Arrays.asList(event));

        List<EventDTO> events = eventService.getEventsByDate(date, user.getId());

        assertEquals(1, events.size());
        assertEquals("Test Event", events.get(0).getName());
        verify(eventRepository, times(1)).findByDateAndUserId(date, user.getId());
    }

    @Test
    void testDeleteEvent_Success() {
        Long eventId = 1L;
        Event event = new Event();
        event.setId(eventId);
        event.setUser(user);

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        eventService.deleteEvent(eventId, user.getId());

        verify(eventRepository, times(1)).deleteById(eventId);
    }

    @Test
    void testDeleteEvent_EventNotFound() {
        Long eventId = 1L;

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EventNotFoundException.class, () -> {
            eventService.deleteEvent(eventId, user.getId());
        });

        assertEquals("Event not found or does not belong to the user.", exception.getMessage());
        verify(eventRepository, never()).deleteById(eventId);
    }

  
}
