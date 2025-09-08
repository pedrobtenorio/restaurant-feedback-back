package com.ace4.RestaurantFeedback.service;

import com.ace4.RestaurantFeedback.Exception.EntityNotFoundException;
import com.ace4.RestaurantFeedback.model.DiningTable;
import com.ace4.RestaurantFeedback.model.Dish;
import com.ace4.RestaurantFeedback.model.Feedback;
import com.ace4.RestaurantFeedback.model.DishFeedback;
import com.ace4.RestaurantFeedback.repository.DiningTableRepository;
import com.ace4.RestaurantFeedback.repository.DishRepository;
import com.ace4.RestaurantFeedback.repository.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
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
    private DiningTableRepository diningTableRepository;

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    private Feedback feedback;
    private DiningTable diningTable;
    private Dish dish;
    private DishFeedback dishFeedback;

    @BeforeEach
    void setUp() {
        diningTable = new DiningTable();
        diningTable.setId(1L);

        dish = new Dish();
        dish.setId(1L);
        dish.setName("Pizza");

        dishFeedback = new DishFeedback();
        dishFeedback.setDish(dish);
        dishFeedback.setRating(5);
        dishFeedback.setComment("Excelente!");

        feedback = new Feedback();
        feedback.setId(1L);
        feedback.setTable(diningTable);
        feedback.setDishFeedbackList(Arrays.asList(dishFeedback));
    }

    @Test
    void save_WithValidData_ShouldReturnSavedFeedback() {
        // Arrange
        when(diningTableRepository.existsById(1L)).thenReturn(true);
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        // Act
        Feedback savedFeedback = feedbackService.save(feedback);

        // Assert
        assertNotNull(savedFeedback);
        assertEquals(1L, savedFeedback.getId());
        verify(diningTableRepository, times(1)).existsById(1L);
        verify(dishRepository, times(1)).findById(1L);
        verify(feedbackRepository, times(1)).save(feedback);
    }

    @Test
    void save_WithNonExistingTable_ShouldThrowEntityNotFoundException() {
        // Arrange
        when(diningTableRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, 
            () -> feedbackService.save(feedback));
        
        assertEquals("DiningTable", exception.getEntity());
        assertEquals("1", exception.getId());
        verify(diningTableRepository, times(1)).existsById(1L);
        verify(dishRepository, never()).findById(anyLong());
        verify(feedbackRepository, never()).save(any());
    }

    @Test
    void save_WithNonExistingDish_ShouldThrowEntityNotFoundException() {
        // Arrange
        when(diningTableRepository.existsById(1L)).thenReturn(true);
        when(dishRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, 
            () -> feedbackService.save(feedback));
        
        assertEquals("Dish", exception.getEntity());
        assertEquals("1", exception.getId());
        verify(diningTableRepository, times(1)).existsById(1L);
        verify(dishRepository, times(1)).findById(1L);
        verify(feedbackRepository, never()).save(any());
    }

    @Test
    void save_WithoutDishFeedbackList_ShouldSaveSuccessfully() {
        // Arrange
        feedback.setDishFeedbackList(null);
        when(diningTableRepository.existsById(1L)).thenReturn(true);
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        // Act
        Feedback savedFeedback = feedbackService.save(feedback);

        // Assert
        assertNotNull(savedFeedback);
        assertNull(savedFeedback.getDishFeedbackList());
        verify(diningTableRepository, times(1)).existsById(1L);
        verify(dishRepository, never()).findById(anyLong());
        verify(feedbackRepository, times(1)).save(feedback);
    }

    @Test
    void findAll_ShouldReturnListOfFeedbacks() {
        // Arrange
        List<Feedback> feedbacks = Arrays.asList(feedback);
        when(feedbackRepository.findAll()).thenReturn(feedbacks);

        // Act
        List<Feedback> result = feedbackService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(feedback, result.get(0));
        verify(feedbackRepository, times(1)).findAll();
    }

    @Test
    void findById_WithExistingId_ShouldReturnFeedback() {
        // Arrange
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));

        // Act
        Optional<Feedback> result = feedbackService.findById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(feedback, result.get());
        verify(feedbackRepository, times(1)).findById(1L);
    }

    @Test
    void findById_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        when(feedbackRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<Feedback> result = feedbackService.findById(1L);

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

    @Test
    void save_WithEmptyDishFeedbackList_ShouldSaveSuccessfully() {
        // Arrange
        feedback.setDishFeedbackList(Arrays.asList());
        when(diningTableRepository.existsById(1L)).thenReturn(true);
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        // Act
        Feedback savedFeedback = feedbackService.save(feedback);

        // Assert
        assertNotNull(savedFeedback);
        assertTrue(savedFeedback.getDishFeedbackList().isEmpty());
        verify(diningTableRepository, times(1)).existsById(1L);
        verify(dishRepository, never()).findById(anyLong());
        verify(feedbackRepository, times(1)).save(feedback);
    }

    @Test
    void save_WithMultipleDishFeedbacks_ShouldSaveSuccessfully() {
        // Arrange
        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setName("Hamburger");

        DishFeedback dishFeedback2 = new DishFeedback();
        dishFeedback2.setDish(dish2);
        dishFeedback2.setRating(4);
        dishFeedback2.setComment("Muito bom");

        feedback.setDishFeedbackList(Arrays.asList(dishFeedback, dishFeedback2));

        when(diningTableRepository.existsById(1L)).thenReturn(true);
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(dishRepository.findById(2L)).thenReturn(Optional.of(dish2));
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        // Act
        Feedback savedFeedback = feedbackService.save(feedback);

        // Assert
        assertNotNull(savedFeedback);
        assertEquals(2, savedFeedback.getDishFeedbackList().size());
        verify(diningTableRepository, times(1)).existsById(1L);
        verify(dishRepository, times(1)).findById(1L);
        verify(dishRepository, times(1)).findById(2L);
        verify(feedbackRepository, times(1)).save(feedback);
    }
}