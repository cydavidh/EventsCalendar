package cydavidh.eventscalendar.business;//package cydavidh.springdemo.webcalendar.business;

import cydavidh.eventscalendar.persistence.Event;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@ExtendWith(MockitoExtension.class)
public class EventServiceUnitTest {

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private EventService eventService;

    @Mock
    private EntityTransaction entityTransaction;

    @Test
    public void testSaveEvent() {
        // Given
        Event event = new Event(1, "Test Event", LocalDate.of(2022, 10, 15));

        // When
        eventService.saveEvent(event);

        // Then
        verify(entityManager).persist(event);
    }
    // test for using entityManagerFactory instead of testing the other way: @PersistenceContext to inject an EntityManager directly
//    @Test
//    public void testSaveEvent() {
//        Event event = new Event(1, "hello", LocalDate.of(2022, 10,15)); // Assuming Event is a class with appropriate setters to set properties
//        // Configure your test event as needed
//
//        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
//        when(entityManager.getTransaction()).thenReturn(entityTransaction);
//
//        eventService.saveEvent(event);
//
//        verify(entityManager).persist(event);
//        verify(entityTransaction).begin();
//        verify(entityTransaction).commit();
//    }

    @Mock
    private TypedQuery<Event> typedQuery;

    @Test
    public void testGetAllEvents() {
        // Prepare mock events
        Event event1 = new Event(1, "Event 1", LocalDate.of(2022, 10, 15));
        Event event2 = new Event(2, "Event 2", LocalDate.of(2023, 1, 20));
        List<Event> mockEvents = Arrays.asList(event1, event2);

        // Mock the behavior of entityManager and typedQuery
        when(entityManager.createQuery("SELECT e FROM Event e", Event.class)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(mockEvents);

        // Execute the method to be tested
        List<Event> events = eventService.getAllEvents();

        // Verify the interaction with entityManager and typedQuery
        verify(entityManager).createQuery("SELECT e FROM Event e", Event.class);
        verify(typedQuery).getResultList();

        // Assert that the returned list matches the mockEvents
        assertEquals(mockEvents, events, "The returned list of events should match the predefined mock events.");
    }

//    @Test
//    public void testGetTodayEvents() {
//        // Prepare mock events for today
//        LocalDate today = LocalDate.now();
//        Event event1 = new Event(1, "Today's Event 1", today);
//        Event event2 = new Event(2, "Today's Event 2", today);
//        Event event3 = new Event(3, "Future Event 3", LocalDate.of(2029, 10, 15));
//        Event event4 = new Event(4, "Past Event 3", LocalDate.of(2019, 9, 15));
//        List<Event> allEvents = Arrays.asList(event1, event2, event3, event4);
//        List<Event> expectedEvents = Arrays.asList(event1, event2);
//
//
//        // Mock the behavior of entityManager.createQuery and TypedQuery.getResultList
//        when(entityManager.createQuery("SELECT e FROM Event e WHERE e.date = :today", Event.class)).thenReturn(typedQuery);
//        when(typedQuery.setParameter("today", today)).thenReturn(typedQuery);
//        when(typedQuery.getResultList()).thenReturn(allEvents);
//
//        // Execute the method to be tested
//        List<Event> eventsToday = eventService.getTodayEvents();
//
//        // Verify the interaction with entityManager and typedQuery
//        verify(entityManager).createQuery("SELECT e FROM Event e WHERE e.date = :today", Event.class);
//        verify(typedQuery).setParameter("today", today);
//        verify(typedQuery).getResultList();
//
//        // Assert that the returned list matches the mockEventsToday
//        assertEquals(expectedEvents, eventsToday, "The returned list of events should match the predefined mock events for today.");
//    }

    @Test
    public void testFindEventsToday() {
        // Given
        LocalDate today = LocalDate.now();
        Event todayEvent1 = new Event(1, "Today's Event 1", today);
        Event todayEvent2 = new Event(2, "Today's Event 2", today);
        Event futureEvent = new Event(3, "Future Event", LocalDate.of(2029, 10, 15));
        entityManager.persist(todayEvent1);
        entityManager.persist(todayEvent2);
        entityManager.persist(futureEvent);
        entityManager.flush();

        // When
        List<Event> eventsToday = eventService.getTodayEvents(); // Assuming this method exists

        // Then
        assertThat(eventsToday).hasSize(2).containsExactlyInAnyOrder(todayEvent1, todayEvent2);
    }

}

