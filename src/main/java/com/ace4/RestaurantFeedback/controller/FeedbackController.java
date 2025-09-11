package com.ace4.RestaurantFeedback.controller;

import com.ace4.RestaurantFeedback.Exception.EntityNotFoundException;
import com.ace4.RestaurantFeedback.model.dto.feedback.FeedbackRequest;
import com.ace4.RestaurantFeedback.model.dto.feedback.FeedbackResponse;
import com.ace4.RestaurantFeedback.service.FeedbackService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public List<FeedbackResponse> getAll() {
        return feedbackService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackResponse> getById(@PathVariable Long id) {
        return feedbackService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (feedbackService.findById(id).isPresent()) {
            feedbackService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}