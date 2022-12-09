package com.alfa1.cv.backoffice.data.service;

import com.alfa1.cv.backoffice.data.entity.Training;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;

    public Training addTraining(Training training) {
        return trainingRepository.save(training);
    }

    public void deleteTraining(Training training) {
        trainingRepository.delete(training);
    }

    public List<Training> getTrainings() {
        return trainingRepository.findAll();
    }
}
