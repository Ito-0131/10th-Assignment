package com.trainer.name.controller.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TrainerRequest {
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, message = "Name must be at least 1 character")
    private String name;

    @NotBlank(message = "Email cannot be blank")
    @Size(min = 1, message = "Email must be at least 1 character")
    private String email;

    public TrainerRequest(String name, String email) {
        if (name == null || email == null) {
            throw new IllegalArgumentException("名前とメールアドレスはnullにできません");
        }
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

}
