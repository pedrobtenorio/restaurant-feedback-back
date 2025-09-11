package com.ace4.RestaurantFeedback.service;

import com.ace4.RestaurantFeedback.Exception.EntityNotFoundException;
import com.ace4.RestaurantFeedback.model.Dish;
import com.ace4.RestaurantFeedback.model.DishFeedback;
import com.ace4.RestaurantFeedback.model.Feedback;
import com.ace4.RestaurantFeedback.model.dto.feedback.FeedbackRequest;
import com.ace4.RestaurantFeedback.model.dto.feedback.FeedbackResponse;
import com.ace4.RestaurantFeedback.repository.DishRepository;
import com.ace4.RestaurantFeedback.repository.FeedbackRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final DishRepository dishRepository;

    public FeedbackResponse save(FeedbackRequest feedbackRequest) {
        Feedback feedback = convertToEntity(feedbackRequest);

        if (feedback.getDishFeedbackList() != null) {
            feedback.getDishFeedbackList().forEach(df -> {
                Dish dish = findDishById(df.getDish().getId());
                df.setDish(dish);
                df.setFeedback(feedback);
            });
        }

        Feedback savedFeedback = feedbackRepository.save(feedback);
        return convertToResponse(savedFeedback);
    }

    public List<FeedbackResponse> findAll() {
        return feedbackRepository.findAll().stream()
                .map(this::convertToResponse)
                .toList();
    }

    public Optional<FeedbackResponse> findById(Long id) {
        return feedbackRepository.findById(id)
                .map(this::convertToResponse);
    }

    public void deleteById(Long id) {
        feedbackRepository.deleteById(id);
    }

    private Dish findDishById(Long dishId) {
        return dishRepository.findById(dishId)
                .orElseThrow(() -> new EntityNotFoundException("Dish", String.valueOf(dishId)));
    }

    private Feedback convertToEntity(FeedbackRequest request) {
        Feedback feedback = new Feedback();
        feedback.setAttendantName(request.attendantName());
        feedback.setServiceRating(request.serviceRating());
        feedback.setFoodRating(request.foodRating());
        feedback.setEnvironmentRating(request.environmentRating());
        feedback.setRecommendationRating(request.recommendationRating());
        feedback.setServiceComment(request.serviceComment());
        feedback.setFoodComment(request.foodComment());
        feedback.setEnvironmentComment(request.environmentComment());
        feedback.setGeneralComment(request.generalComment());
        feedback.setTimestamp(LocalDateTime.now());

        if (request.dishFeedbacks() != null) {
            List<DishFeedback> dishFeedbacks = request.dishFeedbacks().stream()
                    .map(df -> {
                        DishFeedback dishFeedback = new DishFeedback();
                        Dish dish = new Dish();
                        dish.setId(df.dishId());
                        dishFeedback.setDish(dish);
                        dishFeedback.setRating(df.rating());
                        dishFeedback.setComment(df.comments());
                        return dishFeedback;
                    })
                    .toList();
            feedback.setDishFeedbackList(dishFeedbacks);
        }

        return feedback;
    }

    private FeedbackResponse convertToResponse(Feedback feedback) {
        List<FeedbackResponse.DishFeedbackResponse> dishFeedbackResponses = null;

        if (feedback.getDishFeedbackList() != null) {
            dishFeedbackResponses = feedback.getDishFeedbackList().stream()
                    .map(df -> new FeedbackResponse.DishFeedbackResponse(
                            df.getId(),
                            df.getDish().getName(),
                            df.getRating(),
                            df.getComment()
                    ))
                    .toList();
        }

        return new FeedbackResponse(
                feedback.getAttendantName(),
                feedback.getServiceRating(),
                feedback.getFoodRating(),
                feedback.getEnvironmentRating(),
                feedback.getRecommendationRating(),
                feedback.getServiceComment(),
                feedback.getFoodComment(),
                feedback.getEnvironmentComment(),
                feedback.getGeneralComment(),
                feedback.getTimestamp(),
                dishFeedbackResponses
        );
    }
}