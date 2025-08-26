package com.ace4.RestaurantFeedback.repository;

import com.ace4.RestaurantFeedback.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmailIgnoreCase(String email);
}
