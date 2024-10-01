package dev.doel.TDoh.agenda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;



@RestController
@RequestMapping("${api-endpoint}/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<EventDTO> addEvent(@RequestBody EventDTO eventDTO) {
        EventDTO createdEvent = eventService.createEvent(eventDTO, null);
        return ResponseEntity.ok(createdEvent);
    }

    @GetMapping("/{date}")
    public ResponseEntity<List<EventDTO>> getEventsByDate(@PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<EventDTO> events = eventService.getEventsByDate(localDate);
        return ResponseEntity.ok(events);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
