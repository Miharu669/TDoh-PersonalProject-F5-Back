package dev.doel.TDoh.agenda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.doel.TDoh.agenda.agenda_exceptions.EventNotFoundException;
import dev.doel.TDoh.users.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public EventDTO createEvent(EventDTO eventDTO, Long userId) {
        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDate(eventDTO.getDate());

        User user = new User(); 
        user.setId(userId); 
        event.setUser(user); 

        return convertToDTO(eventRepository.save(event));
    }

    public List<EventDTO> getAllEvents(Long userId) {
        return eventRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<EventDTO> getEventsByDate(LocalDate date, Long userId) {
        return eventRepository.findByDateAndUserId(date, userId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public EventDTO getEventById(Long id, Long userId) {
        Optional<Event> eventOptional = eventRepository.findByIdAndUserId(id, userId);
        if (eventOptional.isPresent()) {
            return convertToDTO(eventOptional.get());
        } else {
            throw new EventNotFoundException("Event not found or does not belong to the user.");
        }
    }

    public void deleteEvent(Long id, Long userId) {
        Optional<Event> eventOptional = eventRepository.findById(id);
        if (eventOptional.isPresent() && eventOptional.get().getUser().getId().equals(userId)) {
            eventRepository.deleteById(id);
        } else {
            throw new EventNotFoundException("Event not found or does not belong to the user.");
        }
    }

    public void deleteEventsByDate(LocalDate date, Long userId) {
        int deletedCount = eventRepository.deleteByDateAndUserId(date, userId);
        if (deletedCount == 0) {
            throw new EventNotFoundException("No events found on " + date + " for the user.");
        }
    }

    private EventDTO convertToDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDate(event.getDate());
        return dto;
    }
}
