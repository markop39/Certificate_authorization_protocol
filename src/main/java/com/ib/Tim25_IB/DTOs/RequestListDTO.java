package com.ib.Tim25_IB.DTOs;

import com.ib.Tim25_IB.model.CertificateRequest;
import lombok.Data;

import java.util.List;
@Data
public class RequestListDTO {
    private int length;
    private List<CertificateRequest> certificates;

    public RequestListDTO(int size, List<CertificateRequest> requestList) {
        this.length = size;
        this.certificates = requestList;
    }
}
