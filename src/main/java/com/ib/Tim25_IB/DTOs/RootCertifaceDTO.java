package com.ib.Tim25_IB.DTOs;

public class RootCertifaceDTO {
    private String subjectUsername;
    private String keyUsageFlags;
    private String validTo;

    public RootCertifaceDTO() {
    }

    public RootCertifaceDTO(String subjectUsername, String keyUsageFlags, String validTo) {
        this.subjectUsername = subjectUsername;
        this.keyUsageFlags = keyUsageFlags;
        this.validTo = validTo;
    }

    public String getSubjectUsername() {
        return subjectUsername;
    }

    public void setSubjectUsername(String subjectUsername) {
        this.subjectUsername = subjectUsername;
    }

    public String getKeyUsageFlags() {
        return keyUsageFlags;
    }

    public void setKeyUsageFlags(String keyUsageFlags) {
        this.keyUsageFlags = keyUsageFlags;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }
}
