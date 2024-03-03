package cydavidh.eventscalendar.business;

import cydavidh.eventscalendar.persistence.Event;
import jakarta.persistence.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveEvent(Event event) {
        entityManager.merge(event);
        if (event.getId() > 0) {
            entityManager.merge(event);
        } else {
            entityManager.persist(event);
        }
    }

    @Transactional(readOnly = true)
    public List<Event> getAllEvents() {
        TypedQuery<Event> query = entityManager.createQuery("SELECT e FROM Event e", Event.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public List<Event> getTodayEvents() {
        TypedQuery<Event> query = entityManager.createQuery("SELECT e FROM Event e WHERE e.eventDate = :today", Event.class);
        query.setParameter("today", LocalDate.now());
        return query.getResultList();
    }

    @Transactional
    public Event getEventById(long id) {
        Event event = entityManager.find(Event.class, id);
        return event;
    }

    @Transactional
    public Event deleteEventById(long id) {
        Event event = entityManager.find(Event.class, id);
        if (event == null) {
            return null;
        }
        entityManager.remove(event);
        return event;
    }
    @Transactional
    public List<Event> getEventByDatePeriod(LocalDate start, LocalDate end) {
        if (start != null && end != null) {
            return entityManager.createQuery("SELECT e FROM Event e WHERE e.eventDate BETWEEN :start AND :end", Event.class)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .getResultList();
        } else if (start != null) {
            return entityManager.createQuery("SELECT e FROM Event e WHERE e.eventDate >= :start", Event.class)
                    .setParameter("start", start)
                    .getResultList();
        } else if (end != null) {
            return entityManager.createQuery("SELECT e FROM Event e WHERE e.eventDate <= :end", Event.class)
                    .setParameter("end", end)
                    .getResultList();
        } else {
            return getAllEvents();
        }
    }

}

//version implemented with EntityManagerFactory, allows for more control.

//@Service
//public class EventService {
//
//    @Autowired
//    private EntityManagerFactory entityManagerFactory;
//
//    public void saveEvent(Event event) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        try {
//            entityManager.getTransaction().begin();
//            entityManager.persist(event);
//            entityManager.getTransaction().commit();
//        } catch (RuntimeException e) {
//            entityManager.getTransaction().rollback();
//            throw e; // or handle more gracefully
//        } finally {
//            if (entityManager.isOpen()) {
//                entityManager.close();
//            }
//        }
//    }
//
//    public List<Event> getAllEvents() {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        try {
//            TypedQuery<Event> query = entityManager.createQuery("SELECT e FROM Event e", Event.class);
//            return query.getResultList();
//        } finally {
//            if (entityManager.isOpen()) {
//                entityManager.close();
//            }
//        }
//    }
//}