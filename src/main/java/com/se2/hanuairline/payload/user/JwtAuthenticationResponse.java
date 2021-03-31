package com.se2.hanuairline.payload.user;

import com.se2.hanuairline.model.user.User;

public class JwtAuthenticationResponse {
    private String accessToken;
    private User user;
    private String tokenType = "Bearer";

    public JwtAuthenticationResponse(String accessToken, User user) {
        this.accessToken = accessToken;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
