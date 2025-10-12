package com.ace4.RestaurantFeedback.service;

import com.ace4.RestaurantFeedback.Exception.EntityNotFoundException;
import com.ace4.RestaurantFeedback.model.Dish;
import com.ace4.RestaurantFeedback.model.dto.dish.DishRequest;
import com.ace4.RestaurantFeedback.model.dto.dish.DishResponse;
import com.ace4.RestaurantFeedback.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishService {

    private final DishRepository dishRepository;

    // Converter Dish para DishResponse
    private DishResponse convertToResponse(Dish dish) {
        DishResponse response = new DishResponse();
        response.setId(dish.getId());
        response.setName(dish.getName());
        response.setDescription(dish.getDescription());
        return response;
    }

    // Converter DishRequest para Dish
    private Dish convertToEntity(DishRequest request) {
        Dish dish = new Dish();
        dish.setName(request.getName());
        dish.setDescription(request.getDescription());
        return dish;
    }

    public List<DishResponse> findAll() {
        return dishRepository.findAll().stream()
                .filter(dish -> !dish.isDeleted())  // Ignora pratos marcados como deletados
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public DishResponse findById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dish", id.toString()));
        return convertToResponse(dish);
    }

    public DishResponse create(DishRequest dishRequest) {
        Dish dish = convertToEntity(dishRequest);
        Dish savedDish = dishRepository.save(dish);
        return convertToResponse(savedDish);
    }

    public DishResponse update(Long id, DishRequest dishRequest) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dish", id.toString()));

        dish.setName(dishRequest.getName());
        dish.setDescription(dishRequest.getDescription());
        Dish updatedDish = dishRepository.save(dish);
        return convertToResponse(updatedDish);
    }
    public void deleteById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dish", id.toString()));
        dish.setDeleted(true); // marque o prato como deletado
        dishRepository.save(dish); // salve a alteração
    }

}