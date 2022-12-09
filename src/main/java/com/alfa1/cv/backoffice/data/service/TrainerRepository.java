package com.alfa1.cv.backoffice.data.service;

import com.alfa1.cv.backoffice.data.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
}
