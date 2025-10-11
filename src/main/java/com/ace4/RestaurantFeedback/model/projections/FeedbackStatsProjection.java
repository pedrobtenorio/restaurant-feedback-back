package com.ace4.RestaurantFeedback.model.projections;

public interface FeedbackStatsProjection {
    Long getTotalFeedbacks();
    Double getAverageRating();
    Long getFiveStars();
    Long getFourStars();
    Long getThreeStars();
    Long getTwoStars();
    Long getOneStar();
}