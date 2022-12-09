package com.alfa1.cv.backoffice.data.service;

import com.alfa1.cv.backoffice.data.entity.Trainer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;

    public Trainer addTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    public List<Trainer> getTrainers() {
        return trainerRepository.findAll();
    }

    public Trainer getTrainer(Long id) {
        return trainerRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Could not find trainer with id " + id));
    }

    public Trainer updateTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    public void deleteTrainer(Long id) {
        trainerRepository.findById(id).ifPresent(trainerRepository::delete);
    }

}
