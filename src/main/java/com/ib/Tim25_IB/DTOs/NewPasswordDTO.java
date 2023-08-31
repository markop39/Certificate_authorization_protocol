package com.ib.Tim25_IB.DTOs;

import lombok.Data;

@Data
public class NewPasswordDTO {
    private int code;
    private String email;
    private String newPassword;
    private String passwordConfirmation;
}
