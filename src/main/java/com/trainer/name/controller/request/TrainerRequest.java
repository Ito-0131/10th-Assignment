package com.trainer.name.controller.request;

import com.trainer.name.exception.NoFullWidthSpaceValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TrainerRequest {
    @NotBlank(message = "名前は必須項目です")
    @Size(min = 1, message = "Name must be at least 1 character")
    private String name;

    @NoFullWidthSpaceValidator.NoFullWidthSpace(message = "全角スペースは使用できません")
    @NotBlank(message = "メールアドレスは必須項目です")
    @Size(min = 1, message = "Email must be at least 1 character")
    private String email;

    public TrainerRequest(String name, String email) {
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
