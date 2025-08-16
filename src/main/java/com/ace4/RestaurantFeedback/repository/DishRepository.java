package com.ace4.RestaurantFeedback.repository;

import com.ace4.RestaurantFeedback.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {
}

