package com.ituwei.myblog.payload;

import lombok.Data;

@Data
public class JwtAuthenticationRequest {
    private String accessToken;
    private String tokenType="bearer";

    public JwtAuthenticationRequest(String accessToken) {
        this.accessToken = accessToken;
    }
}
