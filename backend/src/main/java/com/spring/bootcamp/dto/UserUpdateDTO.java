package com.spring.bootcamp.dto;

import com.spring.bootcamp.services.validation.UserInsertValid;
import com.spring.bootcamp.services.validation.UserUpdateValid;

import java.io.Serial;

@UserUpdateValid
public class UserUpdateDTO extends UserDTO{

    @Serial
    private static final long serialVersionUID = 1L;

}
