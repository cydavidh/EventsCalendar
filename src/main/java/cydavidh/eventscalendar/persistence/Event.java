package cydavidh.eventscalendar.persistence;

//import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.NotNull;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotEmpty(message = "The event name must not be empty.")
    private String eventName;
    @NotNull(message = "The event date must not be null.")
    private LocalDate eventDate;

    public Event() {
    }

    public Event(String name, LocalDate date) {
        this.eventName = name;
        this.eventDate = date;
    }

    public Event(long id, String name, LocalDate date) {
        this.id = id;
        this.eventName = name;
        this.eventDate = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String event) {
        this.eventName = event;
    }

    public LocalDate getEventDate() { // Method name aligned with field name
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) { // Method name aligned with field name
        this.eventDate = eventDate;
    }
}
