package com.ib.Tim25_IB.services;

import com.ib.Tim25_IB.DTOs.CertificateDTO;
import com.ib.Tim25_IB.DTOs.CertificateListDTO;
import com.ib.Tim25_IB.DTOs.CertificateRequestDTO;
import com.ib.Tim25_IB.Repository.CertificateRepository;
import com.ib.Tim25_IB.Repository.RequestRepository;
import com.ib.Tim25_IB.model.Certificate;
import com.ib.Tim25_IB.model.CertificateRequest;
import com.ib.Tim25_IB.model.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
@Service
public class CertificateService {

    @Autowired
    CertificateRepository certificateRepository;

    public List<Certificate> getAll() throws IOException {
        List<Certificate> certificateList = certificateRepository.getAllCertificates();
        return certificateList;
    }

    public List<Certificate> revokeAll(List<Certificate> list) throws IOException {
        return certificateRepository.revokeAll(list);
    }

    public Certificate getOne(String serialNumber) throws IOException{
        Certificate certificate = certificateRepository.findBySerialNumber(serialNumber);
        return certificate;
    }

    public List<Certificate> getAllBySN(String serialNumber) throws IOException {
        List<Certificate> certificateList = certificateRepository.getAllCertificatesSN(serialNumber);
        return certificateList;
    }


}
