package com.codecool.security.model.internal;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccount {
    private Long id;

    private String username;
    private String nickName;
    private String profilePic_;
    private Date dateOfRegistration;
    private String eMail_;

    private double cash;
    private double cashInvested;

    List<StockPurchase> portfolio = new ArrayList<>();

    @JsonManagedReference
    @Builder.Default
    List<Offer> offers = new ArrayList<>();


}
