package com.codecool.security.model.internal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Offer {
    private Long id;

    private Stock stock;
    private double price;
    private int quantity;
    private double totalValue;

    private OfferType offerType;
    private Date offerDate;

    @JsonBackReference
    @ToString.Exclude
    private UserAccount userAccount;
}
