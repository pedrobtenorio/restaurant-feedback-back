package com.ace4.RestaurantFeedback.service;

import com.ace4.RestaurantFeedback.model.DiningTable;
import com.ace4.RestaurantFeedback.repository.DiningTableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiningTableService {

    private DiningTableRepository diningTableRepository;


    public Optional<DiningTable> findById(Long id) {
        return diningTableRepository.findById(id);
    }

    public DiningTable save(DiningTable table) {
        return diningTableRepository.save(table);
    }

    public void deleteById(Long id) {
        diningTableRepository.deleteById(id);
    }
}

