package com.example.events.controllers;

import com.example.events.dto.RegistrationDto;
import com.example.events.models.Event;
import com.example.events.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@CrossOrigin
public class EventController {
    private final EventService eventService;

    @PutMapping("events/register")
    public ResponseEntity<Event> registerToEvent(@RequestBody RegistrationDto registrationDto){
        return eventService.registerToEvent(registrationDto);
    }

    @GetMapping("events")
    public ResponseEntity<List<Event>> getEventsFromToday(){
        return eventService.getEventsFromToday();
    }

    @PostMapping("events")
    public ResponseEntity<Event> saveEvent(@RequestBody Event event){
        return eventService.saveEvent(event);
    }
}
