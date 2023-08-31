package com.ib.Tim25_IB.DTOs;

import com.ib.Tim25_IB.model.CertificateStatus;
import com.ib.Tim25_IB.model.CertificateType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CertificateDTO {

    //Change when the endpoint is implemented
    private Long id;
    public String serialNumber;
    public String signatureAlgorithm;
    public String issuer;
    public LocalDateTime validTo;
    public LocalDateTime validFrom;
    public CertificateStatus certificateStatus;
    public CertificateType certificateType;
    public String username;
}
