package com.ace4.RestaurantFeedback.repository;

import com.ace4.RestaurantFeedback.model.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiningTableRepository extends JpaRepository<DiningTable, Long> {
}

