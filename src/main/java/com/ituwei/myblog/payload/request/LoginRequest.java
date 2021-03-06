package com.ituwei.myblog.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank
    private String UsernameOrEmail;
    @NotBlank
    private String password;

}
