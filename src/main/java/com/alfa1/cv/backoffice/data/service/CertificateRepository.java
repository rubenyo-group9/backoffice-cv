package com.alfa1.cv.backoffice.data.service;

import com.alfa1.cv.backoffice.data.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

}
