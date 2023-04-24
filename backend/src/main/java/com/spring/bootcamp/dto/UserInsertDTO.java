package com.spring.bootcamp.dto;

import com.spring.bootcamp.services.validation.UserInsertValid;

import java.io.Serial;

@UserInsertValid
public class UserInsertDTO extends UserDTO{

    @Serial
    private static final long serialVersionUID = 1L;

    private String password;

    public UserInsertDTO() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
