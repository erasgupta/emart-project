package com.portal.emart.api.model;

//import jakarta.validation.constraints.NotBlank;

public class SendCodeRequest {

   // @NotBlank(message = "Username is required")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
