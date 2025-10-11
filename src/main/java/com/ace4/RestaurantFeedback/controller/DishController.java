package com.ace4.RestaurantFeedback.controller;

import com.ace4.RestaurantFeedback.Exception.EntityNotFoundException;
import com.ace4.RestaurantFeedback.model.dto.dish.DishRequest;
import com.ace4.RestaurantFeedback.model.dto.dish.DishResponse;
import com.ace4.RestaurantFeedback.service.DishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    public List<DishResponse> getAll() {
        return dishService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishResponse> getById(@PathVariable Long id) {
        try {
            DishResponse dish = dishService.findById(id);
            return ResponseEntity.ok(dish);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<DishResponse> create(@RequestBody DishRequest dishRequest) {
        DishResponse savedDish = dishService.create(dishRequest);
        return ResponseEntity.ok(savedDish);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishResponse> update(@PathVariable Long id, @RequestBody DishRequest dishRequest) {
        try {
            DishResponse updatedDish = dishService.update(id, dishRequest);
            return ResponseEntity.ok(updatedDish);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            dishService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}