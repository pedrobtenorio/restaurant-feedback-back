package com.ace4.RestaurantFeedback.model.dto.feedback;

import java.time.LocalDateTime;
import java.util.List;

public record FeedbackResponse(
    String attendantName,
    Integer serviceRating,
    Integer foodRating,
    Integer environmentRating,
    Integer recommendationRating,
    String generalComment,
    LocalDateTime createdAt,
    List<DishFeedbackResponse> dishFeedbacks
) {
    public record DishFeedbackResponse(
        Long id,
        String dishName,
        Integer rating,
        String comments
    ) {}
}
