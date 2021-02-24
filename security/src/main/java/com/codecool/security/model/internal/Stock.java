package com.codecool.security.model.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stock {

    private Long id;
    private String name;
    private String symbol;
    private String logo;
    private Date ipo;
    private float sharesOutstanding;
    private String weburl;
    private String exchange;
    private String country;
    private String industry;
}
