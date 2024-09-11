package com.example.events.repository;

import com.example.events.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.appUsers WHERE e.time >= :start")
    List<Event> findFromDate(@Param("start") Instant yesterday);
}
