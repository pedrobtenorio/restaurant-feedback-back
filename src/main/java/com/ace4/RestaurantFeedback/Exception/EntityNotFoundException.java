package com.ace4.RestaurantFeedback.Exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
    private final String entity;
    private final String id;
    private final String code;

    public EntityNotFoundException(String entity, String id) {
        super(entity + " not found with ID: " + id);
        this.entity = entity;
        this.id = id;
        this.code = "E001";
    }
}
