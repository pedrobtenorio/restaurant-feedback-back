package com.ace4.RestaurantFeedback.service;

import com.ace4.RestaurantFeedback.Exception.EntityNotFoundException;
import com.ace4.RestaurantFeedback.model.Dish;
import com.ace4.RestaurantFeedback.model.Feedback;
import com.ace4.RestaurantFeedback.model.DishFeedback;
import com.ace4.RestaurantFeedback.model.dto.feedback.FeedbackRequest;
import com.ace4.RestaurantFeedback.model.dto.feedback.FeedbackResponse;
import com.ace4.RestaurantFeedback.repository.DishRepository;
import com.ace4.RestaurantFeedback.repository.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    private FeedbackRequest feedbackRequest;
    private Feedback feedback;
    private Dish dish;

    @BeforeEach
    void setUp() {
        dish = new Dish();
        dish.setId(1L);
        dish.setName("Pizza");

        FeedbackRequest.DishFeedbackRequest dishFeedbackRequest =
                new FeedbackRequest.DishFeedbackRequest(1L, 5, "Excelente!");

        feedbackRequest = new FeedbackRequest(
                "João",
                5, 5, 5, 5,
                "Ótimo atendimento",
                "Comida deliciosa",
                "Ambiente agradável",
                "Recomendo!",
                List.of(dishFeedbackRequest)
        );

        feedback = new Feedback();
        feedback.setId(1L);
        feedback.setAttendantName("João");
        feedback.setTimestamp(LocalDateTime.now());

        DishFeedback dishFeedback = new DishFeedback();
        dishFeedback.setId(1L);
        dishFeedback.setDish(dish);
        dishFeedback.setRating(5);
        dishFeedback.setComment("Excelente!");

        feedback.setDishFeedbackList(List.of(dishFeedback));
    }

    @Test
    void save_WithValidData_ShouldReturnFeedbackResponse() {
        // Arrange
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        // Act
        FeedbackResponse savedFeedback = feedbackService.save(feedbackRequest);

        // Assert
        assertNotNull(savedFeedback);
        assertEquals("João", savedFeedback.attendantName());
        assertEquals(1, savedFeedback.dishFeedbacks().size());
        assertEquals("Pizza", savedFeedback.dishFeedbacks().getFirst().dishName());
        verify(dishRepository, times(1)).findById(1L);
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void save_WithNonExistingDish_ShouldThrowEntityNotFoundException() {
        // Arrange
        when(dishRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, 
            () -> feedbackService.save(feedbackRequest));

        assertEquals("Dish", exception.getEntity());
        assertEquals("1", exception.getId());
        verify(dishRepository, times(1)).findById(1L);
        verify(feedbackRepository, never()).save(any());
    }

    @Test
    void save_WithoutDishFeedbackList_ShouldSaveSuccessfully() {
        // Arrange
        FeedbackRequest requestWithoutDish = new FeedbackRequest(
                "João", 5, 5, 5, 5,
                "Ótimo", "Delicioso", "Agradável", "Recomendo!", null
        );
        feedback.setDishFeedbackList(null);
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        // Act
        FeedbackResponse savedFeedback = feedbackService.save(requestWithoutDish);

        // Assert
        assertNotNull(savedFeedback);
        assertNull(savedFeedback.dishFeedbacks());
        verify(dishRepository, never()).findById(anyLong());
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void findAll_ShouldReturnListOfFeedbackResponses() {
        // Arrange
        List<Feedback> feedbacks = Collections.singletonList(feedback);
        when(feedbackRepository.findAll()).thenReturn(feedbacks);

        // Act
        List<FeedbackResponse> result = feedbackService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("João", result.getFirst().attendantName());
        verify(feedbackRepository, times(1)).findAll();
    }

    @Test
    void findById_WithExistingId_ShouldReturnFeedbackResponse() {
        // Arrange
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));

        // Act
        Optional<FeedbackResponse> result = feedbackService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("João", result.get().attendantName());
        verify(feedbackRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        when(feedbackRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<FeedbackResponse> result = feedbackService.findById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(feedbackRepository, times(1)).findById(1L);
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        // Arrange
        doNothing().when(feedbackRepository).deleteById(1L);

        // Act
        feedbackService.deleteById(1L);

        // Assert
        verify(feedbackRepository, times(1)).deleteById(1L);
    }
}