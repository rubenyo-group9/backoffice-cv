package com.alfa1.cv.backoffice.data.service;

import com.alfa1.cv.backoffice.data.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

}
