package cydavidh.eventscalendar.business;

import cydavidh.springdemo.webcalendar.persistence.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(EventService.class)
public class EventServiceIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EventService eventService;

    @BeforeEach
    void setUp() {
        Event todayEvent = new Event();
        todayEvent.setEventDate(LocalDate.now());
        todayEvent.setEventName("Today's Event");
        entityManager.persist(todayEvent);

        Event pastEvent = new Event();
        pastEvent.setEventDate(LocalDate.now().minusDays(1));
        pastEvent.setEventName("Past Event");
        entityManager.persist(pastEvent);

        entityManager.flush();
    }

    @Test
    public void testSaveEvent() {
        Event newEvent = new Event();
        newEvent.setEventName("New Event");
        newEvent.setEventDate(LocalDate.now());

        // Save the new event
        eventService.saveEvent(newEvent);

        // Retrieve all events to verify the new event was added
        List<Event> events = eventService.getAllEvents();

        assertThat(events).hasSize(3); // Assuming 2 events were already added in @BeforeEach
        assertThat(events).extracting(Event::getEventName).contains("New Event");
    }


    @Test
    public void testGetAllEvents() {
        List<Event> events = eventService.getAllEvents();

        assertThat(events).hasSize(2);
    }

    @Test
    public void testGetTodayEvents() {
        List<Event> events = eventService.getTodayEvents();

        assertThat(events).hasSize(1).extracting(Event::getEventName).contains("Today's Event");
    }
}
