package com.ib.Tim25_IB.DTOs;


public class CertificateRequestDTO {
    private String issuerSN;
    private String subjectUsername;
    private String keyUsageFlags;
    private String validTo;

    // Getters and setters
    public String getIssuerSN() { return issuerSN; }
    public void setIssuerSN(String issuerSN) { this.issuerSN = issuerSN; }
    public String getSubjectUsername() { return subjectUsername; }
    public void setSubjectUsername(String subjectUsername) { this.subjectUsername = subjectUsername; }
    public String getKeyUsageFlags() { return keyUsageFlags; }
    public void setKeyUsageFlags(String keyUsageFlags) { this.keyUsageFlags = keyUsageFlags; }
    public String getValidTo() { return validTo; }
    public void setValidTo(String validTo) { this.validTo = validTo; }
}
