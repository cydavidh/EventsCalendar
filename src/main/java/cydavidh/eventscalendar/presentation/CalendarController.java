package cydavidh.eventscalendar.presentation;

import cydavidh.eventscalendar.business.EventService;
import cydavidh.eventscalendar.exceptions.EventNotFoundException;
import cydavidh.eventscalendar.persistence.Event;
import jakarta.validation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class CalendarController {

    private EventService eventService;

    public CalendarController(EventService eventService) {
        this.eventService = eventService;
    }


    @GetMapping(value = "/event/today")
    public ResponseEntity<?> getTodayEvents() {
        List<Event> temp = eventService.getTodayEvents();
        if (temp.isEmpty()) {
            return ResponseEntity.status(204).body(Map.of("message", "No events today."));
        }
        return ResponseEntity.ok(temp);
    }

    @GetMapping("/event")
    public ResponseEntity<?> getAllEvents(@RequestParam(required = false) LocalDate start_time, @RequestParam(required = false) LocalDate end_time) {
        List<Event> events;

        if (start_time != null || end_time != null) {
            events = eventService.getEventByDatePeriod(start_time, end_time);
        } else {
            events = eventService.getAllEvents();
        }

        if (events.isEmpty()) {
            return ResponseEntity.status(204).body(Map.of("message", "No events."));
        }
        return ResponseEntity.ok(events);
    }

    @PostMapping("/event")
    public ResponseEntity<?> addEvent(@RequestBody @Valid Event eventRequest) {

        eventService.saveEvent(eventRequest);

        return ResponseEntity.ok(Map.of(
                "message", "The event has been added!",
                "event", eventRequest.getEventName(),
                "date", eventRequest.getEventDate().toString()
        ));
    }

    @GetMapping("/event/{id}")
    public ResponseEntity<?> getEventById(@PathVariable long id) {
        Event event = eventService.getEventById(id);
        if (event == null) {
            throw new EventNotFoundException("The event doesn't exist!");
        }
        return ResponseEntity.ok(event);
    }

    @DeleteMapping("/event/{id}")
    public ResponseEntity<?> deleteEventById(@PathVariable long id) {
        Event event = eventService.deleteEventById(id);
        if (event == null) {
            throw new EventNotFoundException("The event doesn't exist!");
        }
        return ResponseEntity.ok(event);
    }
}