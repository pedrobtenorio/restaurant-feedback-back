package com.ace4.RestaurantFeedback.repository;

import com.ace4.RestaurantFeedback.model.DishFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishFeedbackRepository extends JpaRepository<DishFeedback, Long> {
}

