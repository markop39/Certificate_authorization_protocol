package com.ib.Tim25_IB.model;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name="requests")
public class CertificateRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String issuerSN;

    @Column(nullable = false)
    private String subjectUsername;

    @Column(nullable = false)
    private String keyUsageFlags;

    @Temporal(TemporalType.TIMESTAMP)
    private Date validTo;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column
    private String statusDeniedMessage;
}
