package com.ace4.RestaurantFeedback.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;

    @ManyToOne
    @JoinColumn(name = "attendant_id")
    private Attendant attendant;

    private Integer serviceRating;
    private Integer foodRating;
    private Integer environmentRating;
    private Integer recommendationRating;

    private String serviceComment;
    private String foodComment;
    private String environmentComment;

    private String generalComment;

    private LocalDateTime timestamp;

    @OneToMany(mappedBy = "feedback", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DishFeedback> dishFeedbackList;

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }
}