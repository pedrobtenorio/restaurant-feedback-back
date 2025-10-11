package com.ace4.RestaurantFeedback.service;

import com.ace4.RestaurantFeedback.Exception.EntityNotFoundException;
import com.ace4.RestaurantFeedback.model.Attendant;
import com.ace4.RestaurantFeedback.model.Dish;
import com.ace4.RestaurantFeedback.model.DishFeedback;
import com.ace4.RestaurantFeedback.model.Feedback;
import com.ace4.RestaurantFeedback.model.dto.FeedbackSummaryDto;
import com.ace4.RestaurantFeedback.model.dto.RatingDistributionDto;
import com.ace4.RestaurantFeedback.model.dto.feedback.FeedbackRequest;
import com.ace4.RestaurantFeedback.model.dto.feedback.FeedbackResponse;
import com.ace4.RestaurantFeedback.model.dto.feedback.FeedbackSummaryResponse;
import com.ace4.RestaurantFeedback.model.filter.FeedbackFilter;
import com.ace4.RestaurantFeedback.repository.AttendantRepository;
import com.ace4.RestaurantFeedback.repository.DishRepository;
import com.ace4.RestaurantFeedback.repository.FeedbackRepository;
import com.ace4.RestaurantFeedback.specification.FeedbackSpecifications;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final DishRepository dishRepository;
    private final AttendantRepository attendantRepository;
    private final EntityManager entityManager;

    public FeedbackResponse save(FeedbackRequest feedbackRequest) {
        Feedback feedback = convertToEntity(feedbackRequest);

        if (feedback.getDishFeedbackList() != null) {
            feedback.getDishFeedbackList().forEach(df -> {
                Dish dish = findDishById(df.getDish().getId());
                df.setDish(dish);
                df.setFeedback(feedback);
            });
        }

        Feedback savedFeedback = feedbackRepository.save(feedback);
        return convertToResponse(savedFeedback);
    }

    public List<FeedbackResponse> findAll() {
        return feedbackRepository.findAll().stream()
                .map(this::convertToResponse)
                .toList();
    }

    public Optional<FeedbackResponse> findById(Long id) {
        return feedbackRepository.findById(id)
                .map(this::convertToResponse);
    }

    public void deleteById(Long id) {
        feedbackRepository.deleteById(id);
    }

    public List<FeedbackResponse> findAllByAttendantId(Long attendantId) {
        return feedbackRepository.findByAttendantId(attendantId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private Dish findDishById(Long dishId) {
        return dishRepository.findById(dishId)
                .orElseThrow(() -> new EntityNotFoundException("Dish", String.valueOf(dishId)));
    }

    private Feedback convertToEntity(FeedbackRequest request) {
        Feedback feedback = new Feedback();

        if (request.attendantId() != null) {
            Attendant attendant = attendantRepository.findById(request.attendantId())
                .orElseThrow(() -> new EntityNotFoundException("Attendant", String.valueOf(request.attendantId())));
            feedback.setAttendant(attendant);
        } else {
            feedback.setAttendant(null);
        }

        feedback.setServiceRating(request.serviceRating());
        feedback.setFoodRating(request.foodRating());
        feedback.setEnvironmentRating(request.environmentRating());
        feedback.setRecommendationRating(request.recommendationRating());
        feedback.setGeneralComment(request.generalComment());
        feedback.setTimestamp(LocalDateTime.now());

        if (request.dishFeedbacks() != null) {
            List<DishFeedback> dishFeedbacks = request.dishFeedbacks().stream()
                    .map(df -> {
                        DishFeedback dishFeedback = new DishFeedback();
                        Dish dish = new Dish();
                        dish.setId(df.dishId());
                        dishFeedback.setDish(dish);
                        dishFeedback.setRating(df.rating());
                        dishFeedback.setComment(df.comments());
                        return dishFeedback;
                    })
                    .toList();
            feedback.setDishFeedbackList(dishFeedbacks);
        }

        return feedback;
    }

    private FeedbackResponse convertToResponse(Feedback feedback) {
        List<FeedbackResponse.DishFeedbackResponse> dishFeedbackResponses = null;

        if (feedback.getDishFeedbackList() != null) {
            dishFeedbackResponses = feedback.getDishFeedbackList().stream()
                    .map(df -> new FeedbackResponse.DishFeedbackResponse(
                            df.getId(),
                            df.getDish().getName(),
                            df.getRating(),
                            df.getComment()
                    ))
                    .toList();
        }

        // Ajuste: retornar attendant info se necessário
        String attendantName = feedback.getAttendant() != null ? feedback.getAttendant().getName() : null;

        return new FeedbackResponse(
                attendantName,
                feedback.getServiceRating(),
                feedback.getFoodRating(),
                feedback.getEnvironmentRating(),
                feedback.getRecommendationRating(),
                feedback.getGeneralComment(),
                feedback.getTimestamp(),
                dishFeedbackResponses
        );
    }

    public Page<FeedbackSummaryResponse> findAllSummariesWithFilters(FeedbackFilter filter, Pageable pageable) {
        return feedbackRepository.findAll(
                FeedbackSpecifications.withFilters(filter),
                pageable
        ).map(feedback -> new FeedbackSummaryResponse(
                feedback.getCustomerName(),
                feedback.getAttendant() != null ? feedback.getAttendant().getName() : null,
                feedback.getServiceRating(),
                feedback.getFoodRating(),
                feedback.getEnvironmentRating(),
                feedback.getRecommendationRating(),
                feedback.getGeneralComment(),
                feedback.getTimestamp()
        ));
    }



    public FeedbackSummaryDto getFeedbackSummary(
            String ratingType,
            String attendantName,
            LocalDate startDate,
            LocalDate endDate) {

        // 1. Validar e mapear o tipo de avaliação para o nome da coluna no BD
        String ratingColumn = mapRatingTypeToColumn(ratingType);

        // 2. Construir a query JPQL dinamicamente
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> parameters = new HashMap<>();

        queryBuilder.append("SELECT ")
                .append(" COUNT(f) AS totalFeedbacks, ")
                .append(" AVG(f.").append(ratingColumn).append(") AS averageRating, ")
                .append(" SUM(CASE WHEN f.").append(ratingColumn).append(" = 5 THEN 1 ELSE 0 END) AS fiveStars, ")
                .append(" SUM(CASE WHEN f.").append(ratingColumn).append(" = 4 THEN 1 ELSE 0 END) AS fourStars, ")
                .append(" SUM(CASE WHEN f.").append(ratingColumn).append(" = 3 THEN 1 ELSE 0 END) AS threeStars, ")
                .append(" SUM(CASE WHEN f.").append(ratingColumn).append(" = 2 THEN 1 ELSE 0 END) AS twoStars, ")
                .append(" SUM(CASE WHEN f.").append(ratingColumn).append(" = 1 THEN 1 ELSE 0 END) AS oneStar ")
                .append("FROM Feedback f WHERE 1=1 ");

        if (attendantName != null && !attendantName.isBlank()) {
            queryBuilder.append("AND f.attendant.name = :attendantName ");
            parameters.put("attendantName", attendantName);
        }
        if (startDate != null) {
            queryBuilder.append("AND f.timestamp >= :startDate ");
            parameters.put("startDate", startDate.atStartOfDay());
        }
        if (endDate != null) {
            queryBuilder.append("AND f.timestamp <= :endDate ");
            parameters.put("endDate", endDate.plusDays(1).atStartOfDay());
        }

        TypedQuery<Object[]> query = entityManager.createQuery(queryBuilder.toString(), Object[].class);
        parameters.forEach(query::setParameter);

        Object[] result = query.getSingleResult();

        return mapResultToDto(result);
    }

    private String mapRatingTypeToColumn(String ratingType) {
        return switch (Objects.requireNonNullElse(ratingType, "service")) {
            case "food" -> "foodRating";
            case "environment" -> "environmentRating";
            case "service" -> "serviceRating";
            default -> throw new IllegalArgumentException("Tipo de avaliação inválido: " + ratingType);
        };
    }

    private FeedbackSummaryDto mapResultToDto(Object[] result) {
        if (result == null || result[0] == null || (Long) result[0] == 0) {
            // Retorna um resumo vazio se não houver feedbacks
            return new FeedbackSummaryDto(0, 0.0, new RatingDistributionDto(0, 0, 0, 0, 0));
        }

        long totalFeedbacks = (Long) result[0];
        double averageRating = (result[1] != null) ? (Double) result[1] : 0.0;
        RatingDistributionDto distribution = new RatingDistributionDto(
                (Long) result[2], (Long) result[3], (Long) result[4], (Long) result[5], (Long) result[6]
        );

        return new FeedbackSummaryDto(totalFeedbacks, averageRating, distribution);
    }
}