package com.codecool.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Trader {
    private Long id;
    private String username;
    private String password;
    @Builder.Default
    private List<String> roles = new ArrayList<>();
}
