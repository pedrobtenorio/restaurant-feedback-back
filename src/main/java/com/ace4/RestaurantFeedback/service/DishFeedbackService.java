package com.ace4.RestaurantFeedback.service;

import com.ace4.RestaurantFeedback.model.DishFeedback;
import com.ace4.RestaurantFeedback.repository.DishFeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DishFeedbackService {


    private DishFeedbackRepository dishFeedbackRepository;

    public List<DishFeedback> findAll() {
        return dishFeedbackRepository.findAll();
    }

    public Optional<DishFeedback> findById(Long id) {
        return dishFeedbackRepository.findById(id);
    }

    public DishFeedback save(DishFeedback dishFeedback) {
        return dishFeedbackRepository.save(dishFeedback);
    }

    public void deleteById(Long id) {
        dishFeedbackRepository.deleteById(id);
    }
}

