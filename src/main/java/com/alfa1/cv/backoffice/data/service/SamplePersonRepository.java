package com.alfa1.cv.backoffice.data.service;

import com.alfa1.cv.backoffice.data.entity.SamplePerson;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SamplePersonRepository extends JpaRepository<SamplePerson, UUID> {

}