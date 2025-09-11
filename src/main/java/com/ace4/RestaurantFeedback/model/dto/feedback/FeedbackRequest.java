package com.ace4.RestaurantFeedback.model.dto.feedback;

import java.util.List;

public record FeedbackRequest(
    String attendantName,
    Integer serviceRating,
    Integer foodRating,
    Integer environmentRating,
    Integer recommendationRating,
    String serviceComment,
    String foodComment,
    String environmentComment,
    String generalComment,
    List<DishFeedbackRequest> dishFeedbacks
) {
    public record DishFeedbackRequest(
        Long dishId,
        Integer rating,
        String comments
    ) {}
}
