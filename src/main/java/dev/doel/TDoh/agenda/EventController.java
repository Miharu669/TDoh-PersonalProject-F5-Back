package dev.doel.TDoh.agenda;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import dev.doel.TDoh.users.User;

import java.time.LocalDate;
import java.util.List;

import java.security.Principal;

@RestController
@RequestMapping("${api-endpoint}/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<EventDTO> addEvent(Principal connectedUser, @RequestBody EventDTO eventDTO) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        EventDTO createdEvent = eventService.createEvent(eventDTO, user.getId());
        return ResponseEntity.ok(createdEvent);
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents(Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        List<EventDTO> events = eventService.getAllEvents(user.getId());
        return ResponseEntity.ok(events);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<EventDTO>> getEventsByDate(Principal connectedUser, @PathVariable String date) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        LocalDate localDate = LocalDate.parse(date);
        List<EventDTO> events = eventService.getEventsByDate(localDate, user.getId());
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(Principal connectedUser, @PathVariable Long id) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        EventDTO event = eventService.getEventById(id, user.getId());
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(Principal connectedUser, @PathVariable Long id) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        eventService.deleteEvent(id, user.getId());
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/date/{date}")
    public ResponseEntity<Void> deleteEventsByDate(Principal connectedUser, @PathVariable String date) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        LocalDate localDate = LocalDate.parse(date);
        eventService.deleteEventsByDate(localDate, user.getId());
        return ResponseEntity.noContent().build();
    }
}
