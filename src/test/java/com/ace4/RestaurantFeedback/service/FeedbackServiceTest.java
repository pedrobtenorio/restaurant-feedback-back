package com.ace4.RestaurantFeedback.service;

import com.ace4.RestaurantFeedback.Exception.EntityNotFoundException;
import com.ace4.RestaurantFeedback.model.Dish;
import com.ace4.RestaurantFeedback.model.Feedback;
import com.ace4.RestaurantFeedback.model.DishFeedback;
import com.ace4.RestaurantFeedback.model.dto.feedback.FeedbackRequest;
import com.ace4.RestaurantFeedback.model.dto.feedback.FeedbackResponse;
import com.ace4.RestaurantFeedback.repository.DishRepository;
import com.ace4.RestaurantFeedback.repository.FeedbackRepository;
import com.ace4.RestaurantFeedback.repository.AttendantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
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

    @Mock
    private AttendantRepository attendantRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    private FeedbackRequest feedbackRequest;
    private Feedback feedback;
    private Dish dish;
    private com.ace4.RestaurantFeedback.model.Attendant attendant;

    @BeforeEach
    void setUp() {
        dish = new Dish();
        dish.setId(1L);
        dish.setName("Pizza");

        attendant = com.ace4.RestaurantFeedback.model.Attendant.builder()
                .id(1L)
                .name("João")
                .email("joao@email.com")
                .active(true)
                .build();

        FeedbackRequest.DishFeedbackRequest dishFeedbackRequest =
                new FeedbackRequest.DishFeedbackRequest(1L, 5, "Excelente!");

        feedbackRequest = new FeedbackRequest(
                1L, // agora é attendantId (Long)
                5, 5, 5, 5,
                "Ótimo atendimento",
                "Comida deliciosa",
                "Ambiente agradável",
                "Recomendo!",
                List.of(dishFeedbackRequest)
        );

        feedback = new Feedback();
        feedback.setId(1L);
        feedback.setAttendant(attendant);
        feedback.setTimestamp(LocalDateTime.now());

        DishFeedback dishFeedback = new DishFeedback();
        dishFeedback.setId(1L);
        dishFeedback.setDish(dish);
        dishFeedback.setRating(5);
        dishFeedback.setComment("Excelente!");

        feedback.setDishFeedbackList(List.of(dishFeedback));
    }

    @Test
    void saveWithValidDataShouldReturnFeedbackResponse() {
        // Arrange
        when(attendantRepository.findById(1L)).thenReturn(Optional.of(attendant));
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        // Act
        FeedbackResponse savedFeedback = feedbackService.save(feedbackRequest);

        // Assert
        assertNotNull(savedFeedback);
        assertEquals("João", savedFeedback.attendantName());
        assertEquals(1, savedFeedback.dishFeedbacks().size());
        assertEquals("Pizza", savedFeedback.dishFeedbacks().getFirst().dishName());
        verify(attendantRepository, times(1)).findById(1L);
        verify(dishRepository, times(1)).findById(1L);
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void saveWithNonExistingDishShouldThrowEntityNotFoundException() {
        // Arrange
        when(attendantRepository.findById(1L)).thenReturn(Optional.of(attendant)); // garantir attendant existente
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
    void saveWithoutDishFeedbackListShouldSaveSuccessfully() {
        // Arrange
        when(attendantRepository.findById(1L)).thenReturn(Optional.of(attendant));
        FeedbackRequest requestWithoutDish = new FeedbackRequest(
                1L, 5, 5, 5, 5,
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
    void findAllShouldReturnListOfFeedbackResponses() {
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
    void findByIdWithExistingIdShouldReturnFeedbackResponse() {
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
    void findByIdWithNonExistingIdShouldReturnEmpty() {
        // Arrange
        when(feedbackRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<FeedbackResponse> result = feedbackService.findById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(feedbackRepository, times(1)).findById(1L);
    }

    @Test
    void deleteByIdShouldCallRepositoryDelete() {
        // Arrange
        doNothing().when(feedbackRepository).deleteById(1L);

        // Act
        feedbackService.deleteById(1L);

        // Assert
        verify(feedbackRepository, times(1)).deleteById(1L);
    }

    @Test
    void saveWithEmptyDishFeedbackListShouldSaveSuccessfully() {
        // Arrange
        when(attendantRepository.findById(1L)).thenReturn(Optional.of(attendant));
        FeedbackRequest requestWithEmptyDishList = new FeedbackRequest(
                1L, 5, 5, 5, 5,
                "Ótimo", "Delicioso", "Agradável", "Recomendo!",
                Collections.emptyList()
        );

        feedback.setDishFeedbackList(Collections.emptyList());
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        // Act
        FeedbackResponse savedFeedback = feedbackService.save(requestWithEmptyDishList);

        // Assert
        assertNotNull(savedFeedback);
        assertTrue(savedFeedback.dishFeedbacks().isEmpty());
        verify(dishRepository, never()).findById(anyLong());
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void saveWithMultipleDishFeedbacksShouldSaveSuccessfully() {
        // Arrange
        when(attendantRepository.findById(1L)).thenReturn(Optional.of(attendant));
        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Hamburger");

        FeedbackRequest.DishFeedbackRequest dishFeedbackRequest1 =
                new FeedbackRequest.DishFeedbackRequest(1L, 5, "Excelente!");
        FeedbackRequest.DishFeedbackRequest dishFeedbackRequest2 =
                new FeedbackRequest.DishFeedbackRequest(2L, 4, "Muito bom");

        FeedbackRequest requestWithMultipleDishes = new FeedbackRequest(
                1L, 5, 5, 5, 5,
                "Ótimo atendimento", "Comida deliciosa", "Ambiente agradável", "Recomendo!",
                List.of(dishFeedbackRequest1, dishFeedbackRequest2)
        );

        DishFeedback dishFeedback2 = new DishFeedback();
        dishFeedback2.setId(2L);
        dishFeedback2.setDish(dish2);
        dishFeedback2.setRating(4);
        dishFeedback2.setComment("Muito bom");

        feedback.setDishFeedbackList(Arrays.asList(feedback.getDishFeedbackList().getFirst(), dishFeedback2));

        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(dishRepository.findById(2L)).thenReturn(Optional.of(dish2));
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        // Act
        FeedbackResponse savedFeedback = feedbackService.save(requestWithMultipleDishes);

        // Assert
        assertNotNull(savedFeedback);
        assertEquals(2, savedFeedback.dishFeedbacks().size());
        verify(dishRepository, times(1)).findById(1L);
        verify(dishRepository, times(1)).findById(2L);
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }
}