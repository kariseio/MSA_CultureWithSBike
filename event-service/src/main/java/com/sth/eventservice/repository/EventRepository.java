package com.sth.eventservice.repository;

import com.sth.eventservice.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    public Event findByEventNm(String eventNm);
    @Query("SELECT e FROM Event e ORDER BY e.id ASC")
    List<Event> findPageableEvents(@Param("startIndex") int startIndex, @Param("size") int size);

}