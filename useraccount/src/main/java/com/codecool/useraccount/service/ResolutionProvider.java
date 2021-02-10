package com.codecool.useraccount.service;

import com.codecool.useraccount.model.Resolution;
import org.springframework.stereotype.Component;

@Component
public class ResolutionProvider {
    public Resolution createResolution(String param){
        if (param.equals("1")){
            return Resolution.MIN1;
        } else if (param.equals("5")){
            return Resolution.MIN5;
        } else if (param.equals("D")){
            return Resolution.DAY;
        }
        return null;
    }
}
