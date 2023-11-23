package com.sth.eventservice.controller;

import com.sth.eventservice.model.dto.EventDTO;
import com.sth.eventservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event-service")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // 주석 처리된 부분은 사용되지 않는 메서드이므로 주석 처리
//    @GetMapping("/updateFromApi")
//    public ResponseEntity<String> updateEventsFromApi() {
//        eventService.updateEventsFromApi();
//        return ResponseEntity.ok("Event API add successful");
//    }

    @GetMapping("/area")
    public ResponseEntity<String> callAreaServiceAndSaveEvents() {
        eventService.callAreaServiceAndSaveEvents();
        return ResponseEntity.ok("Area Service add successful");
    }

    @GetMapping("/")
    public String index() {
        return "hello, this is event service";
    }

    // 주석 처리된 부분은 사용되지 않는 메서드이므로 주석 처리
//    @PostMapping("/saveEventsFromAreaList")
//    public void saveEventsFromAreaList(@RequestBody List<EventDTO> areaList) {
//        eventService.saveEventsFromAreaList(areaList);
//    }

    @PostMapping("/saveEventsToDatabase")
    public void saveEventsToDatabase(@RequestBody List<EventDTO> eventDTOList) {
        eventService.saveEventsToDatabase(eventDTOList);
    }
}
