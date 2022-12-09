package com.alfa1.cv.backoffice.data.service;

import com.alfa1.cv.backoffice.data.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Training, Long> {
}
