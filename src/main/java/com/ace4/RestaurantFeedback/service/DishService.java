package com.ace4.RestaurantFeedback.service;

import com.ace4.RestaurantFeedback.model.Dish;
import com.ace4.RestaurantFeedback.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DishService {


    private DishRepository dishRepository;

    public List<Dish> findAll() {
        return dishRepository.findAll();
    }

    public Optional<Dish> findById(Long id) {
        return dishRepository.findById(id);
    }

    public Dish save(Dish dish) {
        return dishRepository.save(dish);
    }

    public void deleteById(Long id) {
        dishRepository.deleteById(id);
    }
}

