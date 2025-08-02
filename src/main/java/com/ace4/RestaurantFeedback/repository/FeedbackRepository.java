package com.ace4.RestaurantFeedback.repository;

import com.ace4.RestaurantFeedback.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}