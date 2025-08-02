package com.ace4.RestaurantFeedback.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;




@Entity
@Getter
@Setter
public class DiningTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identifier;

}