package com.codecool.useraccount.model.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceOffer {
    private String symbol;
    private String offerType;
    private int quantity;
    private float price;
    private String userName;
}
