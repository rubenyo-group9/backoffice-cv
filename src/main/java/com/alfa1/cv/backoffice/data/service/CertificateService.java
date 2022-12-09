package com.alfa1.cv.backoffice.data.service;

import com.alfa1.cv.backoffice.data.entity.Certificate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;

    public Certificate saveCertificate(Certificate certificate) {
        return certificateRepository.save(certificate);
    }

    public void deleteCertificate(Certificate certificate) {
        certificateRepository.delete(certificate);
    }
}
