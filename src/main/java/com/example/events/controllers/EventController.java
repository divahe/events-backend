package com.example.events.controllers;

import com.example.events.dto.RegistrationDto;
import com.example.events.models.Event;
import com.example.events.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return new ResponseEntity<>(eventService.registerToEvent(registrationDto), HttpStatus.OK);
    }

    @GetMapping("events")
    public ResponseEntity<List<Event>> getEventsFromToday(){
        return new ResponseEntity<>(eventService.getEventsFromYesterday(), HttpStatus.OK);
    }

    @PostMapping("events")
    public ResponseEntity<Event> saveEvent(@RequestBody Event event){
        return new ResponseEntity<>(eventService.saveEvent(event), HttpStatus.OK);
    }
}
