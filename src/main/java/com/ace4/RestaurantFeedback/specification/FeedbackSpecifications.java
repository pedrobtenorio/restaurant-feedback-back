package com.ace4.RestaurantFeedback.specification;

import com.ace4.RestaurantFeedback.model.Feedback;
import com.ace4.RestaurantFeedback.model.filter.FeedbackFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FeedbackSpecifications {

    public static Specification<Feedback> withFilters(FeedbackFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.startDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("timestamp"),
                        filter.startDate().with(LocalTime.MIN)
                ));
            }

            if (filter.endDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("timestamp"),
                        filter.endDate().with(LocalTime.MAX)
                ));
            }

            if (filter.minFoodRating() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("foodRating"), filter.minFoodRating()));
            }
            if (filter.minServiceRating() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("serviceRating"), filter.minServiceRating()));
            }
            if (filter.minEnvironmentRating() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("environmentRating"), filter.minEnvironmentRating()));
            }
            if (filter.minRecommendationRating() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("recommendationRating"), filter.minRecommendationRating()));
            }
            if (filter.attendantName() != null && !filter.attendantName().isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("attendant").get("name")),
                        "%" + filter.attendantName().toLowerCase() + "%"
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}