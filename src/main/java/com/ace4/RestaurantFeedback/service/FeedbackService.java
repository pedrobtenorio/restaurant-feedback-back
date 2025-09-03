package com.ace4.RestaurantFeedback.service;

import com.ace4.RestaurantFeedback.Exception.EntityNotFoundException;
import com.ace4.RestaurantFeedback.model.Dish;
import com.ace4.RestaurantFeedback.model.Feedback;
import com.ace4.RestaurantFeedback.repository.DiningTableRepository;
import com.ace4.RestaurantFeedback.repository.DishRepository;
import com.ace4.RestaurantFeedback.repository.FeedbackRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final DiningTableRepository diningTableRepository;
    private final DishRepository dishRepository;

    public Feedback save(Feedback feedback) {
        validateDiningTable(feedback.getTable().getId());
        if (feedback.getDishFeedbackList() != null) {
            feedback.getDishFeedbackList().forEach(df -> {
                Dish dish = findDishById(df.getDish().getId());
                df.setDish(dish);
                df.setFeedback(feedback);
            });
        }
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> findAll() {
        return feedbackRepository.findAll();
    }
    public List<Feedback> findByAttendantName(String attendantName) { return feedbackRepository.findByAttendantName(attendantName); }
    public Optional<Feedback> findById(Long id) {
        return feedbackRepository.findById(id);
    }

    public void deleteById(Long id) {
        feedbackRepository.deleteById(id);
    }

    private void validateDiningTable(Long tableId) {
        if (!diningTableRepository.existsById(tableId)) {
            throw new EntityNotFoundException("DiningTable", String.valueOf(tableId));
        }
    }

    private Dish findDishById(Long dishId) {
        return dishRepository.findById(dishId)
                .orElseThrow(() -> new EntityNotFoundException("Dish", String.valueOf(dishId)));
    }
}