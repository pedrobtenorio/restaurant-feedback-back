package com.ace4.RestaurantFeedback.controller;

import com.ace4.RestaurantFeedback.model.Feedback;
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
    public ResponseEntity<Feedback> create(@RequestBody Feedback feedback) {
        Feedback saved = feedbackService.save(feedback);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Feedback> getAll() {
        return feedbackService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getById(@PathVariable Long id) {
        return feedbackService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Feedback> update(@PathVariable Long id, @RequestBody Feedback feedback) {
        return feedbackService.findById(id)
                .map(existing -> {
                    existing.setCustomerName(feedback.getCustomerName());
                    existing.setServiceRating(feedback.getServiceRating());
                    existing.setFoodRating(feedback.getFoodRating());
                    existing.setEnvironmentRating(feedback.getEnvironmentRating());
                    existing.setComment(feedback.getComment());
                    existing.setTable(feedback.getTable());
                    existing.setDishFeedbackList(feedback.getDishFeedbackList());
                    Feedback updated = feedbackService.save(existing);
                    return ResponseEntity.ok(updated);
                })
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