package com.ace4.RestaurantFeedback.model.dto.feedback;

import java.time.LocalDateTime;

public record FeedbackSummaryResponse(
        String customerName,
        String attendantName,
        Integer serviceRating,
        Integer foodRating,
        Integer environmentRating,
        Integer recommendationRating,
        String generalComment,
        LocalDateTime timestamp
) {}
