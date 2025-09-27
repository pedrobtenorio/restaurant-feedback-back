package com.ace4.RestaurantFeedback.model.dto.feedback;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

public record FeedbackFilter(
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime startDate,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime endDate,

    Integer minFoodRating,
    Integer minServiceRating,
    Integer minEnvironmentRating,
    Integer minRecommendationRating
) {}
