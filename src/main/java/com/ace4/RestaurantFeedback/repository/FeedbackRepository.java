package com.ace4.RestaurantFeedback.repository;

import com.ace4.RestaurantFeedback.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByAttendantName(String attendantName);
}