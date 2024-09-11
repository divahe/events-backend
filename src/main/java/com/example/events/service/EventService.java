package com.example.events.service;

import com.example.events.dto.RegistrationDto;
import com.example.events.models.AppUser;
import com.example.events.models.Event;
import com.example.events.repository.AppUserRepository;
import com.example.events.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EventService {
    private final AppUserRepository appUserRepository;
    private final EventRepository eventRepository;

    public ResponseEntity<Event> registerToEvent(RegistrationDto registrationDto) {
        AppUser user = saveOrUpdateUser(registrationDto.getAppUser());
        Optional<Event> optionalEvent = eventRepository.findById(registrationDto.getEventId());
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            Set<AppUser> users = event.getAppUsers();
            users.add(user);
            return new ResponseEntity<>(eventRepository.save(event), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private AppUser saveOrUpdateUser(AppUser newUser) {
        Optional<AppUser> optionalUser = appUserRepository.findByIdCode(newUser.getIdCode());
        if (optionalUser.isPresent()) {
            AppUser user = optionalUser.get();
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            return appUserRepository.save(user);
        } else {
            return appUserRepository.save(newUser);
        }
    }
    public ResponseEntity<List<Event>> getEventsFromToday() {
        Instant yesterday = Instant.now().minus(1, ChronoUnit.DAYS);
        List<Event> fromDate = eventRepository.findFromDate(yesterday);
        return new ResponseEntity<>(fromDate, HttpStatus.OK);
    }

    public ResponseEntity<Event> saveEvent(Event event) {
        Event savedEvent = eventRepository.save(event);
        return new ResponseEntity<>(savedEvent, HttpStatus.OK);
    }
}
