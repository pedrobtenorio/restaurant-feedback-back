package com.ace4.RestaurantFeedback.repository;

import com.ace4.RestaurantFeedback.model.Attendant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendantRepository extends JpaRepository<Attendant, Long> {
}
