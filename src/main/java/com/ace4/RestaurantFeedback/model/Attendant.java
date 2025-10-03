package com.ace4.RestaurantFeedback.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "attendants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private Boolean active;

    @OneToMany(mappedBy = "attendant")
    private List<Feedback> feedbacks;

}
