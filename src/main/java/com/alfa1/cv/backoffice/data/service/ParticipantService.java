package com.alfa1.cv.backoffice.data.service;

import com.alfa1.cv.backoffice.data.entity.Participant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository repository;

    public List<Participant> getParticipants() {
        return repository.findAll();
    }

    public Participant addParticipant(Participant participant) {
        return repository.save(participant);
    }

    public Participant update(Participant participant) {
        return repository.save(participant);
    }

    public void deleteParticipant(Participant participant) {
        repository.delete(participant);
    }
}
