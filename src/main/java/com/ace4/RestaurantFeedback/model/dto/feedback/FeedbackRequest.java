package com.ace4.RestaurantFeedback.model.dto.feedback;

import java.util.List;

public record FeedbackRequest(
    Long attendantId,
    Integer serviceRating,
    Integer foodRating,
    Integer environmentRating,
    Integer recommendationRating,
    String generalComment,
    List<DishFeedbackRequest> dishFeedbacks
) {
    public record DishFeedbackRequest(
        Long dishId,
        Integer rating,
        String comments
    ) {}
}
