package com.example.events.service;

import com.example.events.dto.RegistrationDto;
import com.example.events.exceptions.EventNotFoundException;
import com.example.events.models.AppUser;
import com.example.events.models.Event;
import com.example.events.repository.AppUserRepository;
import com.example.events.repository.EventRepository;
import lombok.RequiredArgsConstructor;
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

    public Event registerToEvent(RegistrationDto registrationDto) {
        AppUser user = saveOrUpdateUser(registrationDto.getAppUser());
        Event event = eventRepository.findById(registrationDto
                .getEventId())
                .orElseThrow(() -> new EventNotFoundException("Event not found"));
        Set<AppUser> users = event.getAppUsers();
        users.add(user);
        return eventRepository.save(event);
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

    public List<Event> getEventsFromYesterday() {
        Instant yesterday = Instant.now().minus(1, ChronoUnit.DAYS);
        return eventRepository.findFromDate(yesterday);
    }

    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }
}
