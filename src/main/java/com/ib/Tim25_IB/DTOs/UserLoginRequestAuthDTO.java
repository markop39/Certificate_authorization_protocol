package com.ib.Tim25_IB.DTOs;

import lombok.Data;

@Data
public class UserLoginRequestAuthDTO {
    private String email;
    private String password;
    private int code;
}
