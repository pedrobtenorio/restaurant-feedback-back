package com.ace4.RestaurantFeedback.controller;

import com.ace4.RestaurantFeedback.Exception.EntityNotFoundException;
import com.ace4.RestaurantFeedback.model.dto.feedback.FeedbackRequest;
import com.ace4.RestaurantFeedback.model.dto.feedback.FeedbackResponse;
import com.ace4.RestaurantFeedback.model.filter.FeedbackFilter;
import com.ace4.RestaurantFeedback.model.dto.feedback.FeedbackSummaryResponse;
import com.ace4.RestaurantFeedback.service.FeedbackService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody FeedbackRequest feedbackRequest) {
        try {
            FeedbackResponse saved = feedbackService.save(feedbackRequest);
            return ResponseEntity.ok(saved);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<Page<FeedbackSummaryResponse>> getAllPaginated(@ModelAttribute FeedbackFilter filter, @PageableDefault() Pageable pageable) {
        return ResponseEntity.ok(feedbackService.findAllSummariesWithFilters(filter, pageable));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/all")
    public List<FeedbackResponse> getAll() {
        return feedbackService.findAll();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponse> getById(@PathVariable Long id) {
        return feedbackService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/by-attendant/{attendantId}")
    public List<FeedbackResponse> getAllByAttendantId(@PathVariable Long attendantId) {
        return feedbackService.findAllByAttendantId(attendantId);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (feedbackService.findById(id).isPresent()) {
            feedbackService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}