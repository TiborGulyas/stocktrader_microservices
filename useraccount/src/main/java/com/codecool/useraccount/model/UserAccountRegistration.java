package com.codecool.useraccount.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountRegistration {

    private String username;
    private String nickName;
    private String profilePic_;
    private String eMail_;

}
