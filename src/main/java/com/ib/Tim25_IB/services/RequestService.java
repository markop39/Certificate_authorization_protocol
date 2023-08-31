package com.ib.Tim25_IB.services;

import com.ib.Tim25_IB.DTOs.CertificateRequestDTO;
import com.ib.Tim25_IB.DTOs.EmailDTO;
import com.ib.Tim25_IB.DTOs.RequestListDTO;
import com.ib.Tim25_IB.Repository.RequestRepository;
import com.ib.Tim25_IB.model.CertificateRequest;
import com.ib.Tim25_IB.model.RequestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    @Autowired
    RequestRepository requestRepository;

    public void createCertificateRequest(CertificateRequestDTO requestDTO) {
        CertificateRequest request = new CertificateRequest();
        request.setStatus(RequestStatus.PENDING);
        request.setIssuerSN(requestDTO.getIssuerSN());
        request.setSubjectUsername(requestDTO.getSubjectUsername());
        request.setKeyUsageFlags(requestDTO.getKeyUsageFlags());
        request.setValidTo(Date.from(LocalDateTime.parse(requestDTO.getValidTo()).atZone(ZoneId.systemDefault()).toInstant()));
        request.setStatusDeniedMessage("No message");

        requestRepository.save(request);
        requestRepository.flush();
    }

    public CertificateRequest denyCertificateRequest(Long id){
        Optional<CertificateRequest> certificateRequestOptional = requestRepository.findById(id);
        if(certificateRequestOptional.isPresent()){
            CertificateRequest certificateRequest = certificateRequestOptional.get();
            certificateRequest.setStatus(RequestStatus.DENIED);
            requestRepository.save(certificateRequest);
            return certificateRequest;
        }
        return null;
    }

    public CertificateRequest acceptCertificateRequest(Long id){
        Optional<CertificateRequest> certificateRequestOptional = requestRepository.findById(id);
        if(certificateRequestOptional.isPresent()){
            CertificateRequest certificateRequest = certificateRequestOptional.get();
            certificateRequest.setStatus(RequestStatus.ACCEPTED);
            requestRepository.save(certificateRequest);
            return certificateRequest;
            //napravi novi cert
        }
        return null;
    }

    public RequestListDTO getAll(String email) {
        List<CertificateRequest> requestList = null;
        if(email.equals("admin")){
            requestList = requestRepository.findAll();
        }else{
            requestList = requestRepository.findAllBySubjectUsername(email);
        }
        if(requestList != null){
            return new RequestListDTO(requestList.size(), requestList);
        }else{
            return null;
        }
    }
}
