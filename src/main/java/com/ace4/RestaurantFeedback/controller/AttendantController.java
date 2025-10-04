package com.ace4.RestaurantFeedback.controller;



import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import com.ace4.RestaurantFeedback.model.Attendant;
import com.ace4.RestaurantFeedback.repository.AttendantRepository;
import com.ace4.RestaurantFeedback.model.dto.attendant.AttendantRequest;
import com.ace4.RestaurantFeedback.model.dto.attendant.AttendantResponse;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attendants")
public class AttendantController {

    private final AttendantRepository attendantRepository;

    @GetMapping
    public List<AttendantResponse> getAll() {
        return attendantRepository.findAll()
                .stream()
                .filter(Attendant::getActive)
                .map(attendant -> new AttendantResponse(
                        attendant.getId(),
                        attendant.getName(),
                        attendant.getEmail()
                ))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendantResponse> getById(@PathVariable Long id) {
        return attendantRepository.findById(id)
                .filter(Attendant::getActive)
                .map(attendant -> new AttendantResponse(
                        attendant.getId(),
                        attendant.getName(),
                        attendant.getEmail()
                ))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AttendantResponse> create(@RequestBody AttendantRequest request) {
        Attendant attendant = new Attendant();
        attendant.setName(request.name());
        attendant.setEmail(request.email());
        attendant.setActive(true);
        Attendant saved = attendantRepository.save(attendant);
        AttendantResponse response = new AttendantResponse(saved.getId(), saved.getName(), saved.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AttendantResponse> update(@PathVariable Long id, @RequestBody AttendantRequest request) {
        return attendantRepository.findById(id)
                .map(existing -> {
                    existing.setName(request.name());
                    existing.setEmail(request.email());
                    Attendant updated = attendantRepository.save(existing);
                    AttendantResponse response = new AttendantResponse(updated.getId(), updated.getName(), updated.getEmail());
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return attendantRepository.findById(id)
                .map(existing -> {
                    existing.setActive(false);
                    attendantRepository.save(existing);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
