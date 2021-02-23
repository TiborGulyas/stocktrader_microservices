package com.codecool.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TraderRegistrationForm {
    private String username;
    private String password;
    private String nickName;
    private String profilePic_;
    private String e_mail;
}
