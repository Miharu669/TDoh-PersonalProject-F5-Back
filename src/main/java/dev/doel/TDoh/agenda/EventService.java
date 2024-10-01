package dev.doel.TDoh.agenda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.doel.TDoh.users.User;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public EventDTO createEvent(EventDTO eventDTO, User user) {
        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDate(eventDTO.getDate());
        event.setUser(user); 
        return convertToDTO(eventRepository.save(event));
    }
    

    public List<EventDTO> getEventsByDate(LocalDate date) {
        return eventRepository.findByDate(date)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    private EventDTO convertToDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDate(event.getDate());
        return dto;
    }
}
