package com.ace4.RestaurantFeedback.model.dto;

public record RatingDistributionDto(
        long fiveStars,
        long fourStars,
        long threeStars,
        long twoStars,
        long oneStar
) {}