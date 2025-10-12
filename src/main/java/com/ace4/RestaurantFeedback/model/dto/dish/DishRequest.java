package com.ace4.RestaurantFeedback.model.dto.dish;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishRequest {
    private String name;
    private String description;
    private boolean active;

}