package com.ib.Tim25_IB.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class CertificateListDTO {

    private int length;
    private List<CertificateDTO> certificates;

    public CertificateListDTO(int size, List<CertificateDTO> certificateList) {
        this.length = size;
        this.certificates = certificateList;
    }
}
