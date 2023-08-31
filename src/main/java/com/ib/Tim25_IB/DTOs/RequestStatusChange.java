package com.ib.Tim25_IB.DTOs;

public class RequestStatusChange {
    private Long id;
    private String statusChange;

    public RequestStatusChange() {
    }

    public RequestStatusChange(Long id, String statusChange) {
        this.id = id;
        this.statusChange = statusChange;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatusChange() {
        return statusChange;
    }

    public void setStatusChange(String statusChange) {
        this.statusChange = statusChange;
    }
}
