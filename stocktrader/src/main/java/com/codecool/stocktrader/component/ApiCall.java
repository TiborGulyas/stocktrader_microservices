package com.codecool.stocktrader.component;

import com.codecool.stocktrader.service.ApiStringProvider;
import com.codecool.stocktrader.service.PingHost;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.ConnectException;

@Component
public class ApiCall {

    @Autowired
    private RemoteUrlReader remoteUrlReader;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private PingHost pingHost;

    private ApiStringProvider apiStringProvider;

    public JsonObject getResult(String apiPath) throws IOException {
        String result = remoteUrlReader.readFromUrl(apiPath);
        JsonObject resultJson = new JsonParser()
                .parse(result)
                .getAsJsonObject();
        return resultJson;
    }

    public void postCall() {
        if (pingHost.pingHost("localhost", 8091, 1000)){
            restTemplate.postForLocation(apiStringProvider.matchAllOffer, null);
        }
    }
}

