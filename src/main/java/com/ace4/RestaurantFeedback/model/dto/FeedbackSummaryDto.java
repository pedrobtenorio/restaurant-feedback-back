package com.ace4.RestaurantFeedback.model.dto;

public record FeedbackSummaryDto(
        long totalFeedbacks,
        double averageRating,
        RatingDistributionDto ratingDistribution
) {}