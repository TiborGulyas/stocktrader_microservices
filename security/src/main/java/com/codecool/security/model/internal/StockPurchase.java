package com.codecool.security.model.internal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockPurchase {
    private Long id;

    private Stock stock;
    private double purchasePrice;
    private int quantity;
    private Date purchaseDate;

    @JsonBackReference
    @ToString.Exclude
    private UserAccount userAccount;


}
