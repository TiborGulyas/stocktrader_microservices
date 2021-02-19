package com.codecool.stocktrader.component;

import com.codecool.stocktrader.service.ApiStringProvider;
import com.codecool.stocktrader.service.PingHost;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class ApiCall {

    @Autowired
    private RemoteUrlReader remoteUrlReader;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PingHost pingHost;

    @Autowired
    private ApiStringProvider apiStringProvider;

    public JsonObject getResult(String apiPath) throws IOException {
        String result = remoteUrlReader.readFromUrl(apiPath);
        JsonObject resultJson = new JsonParser()
                .parse(result)
                .getAsJsonObject();
        return resultJson;
    }


    public void postCall() {
        int port = 8091;
        while (port < 8100){
            if (pingHost.pingHost("localhost", port, 100)){
                restTemplate.postForLocation(apiStringProvider.provideApiStringForMatchAllOffers(), null);
                break;
            } else {
                port += 1;
            }
        }
    }
}

