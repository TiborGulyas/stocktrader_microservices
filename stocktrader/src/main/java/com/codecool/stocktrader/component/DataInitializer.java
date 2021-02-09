package com.codecool.stocktrader.component;


import com.codecool.stocktrader.model.*;
import com.codecool.stocktrader.repository.StockRepository;
import com.codecool.stocktrader.service.*;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class DataInitializer {

    @Autowired
    private ApiCall apiCall;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ApiStringProvider apiStringProvider;

    @Autowired
    private PriceDataUpdater priceDataUpdater;

    @Autowired
    private StockLogoProvider stockLogoProvider;

    public static final Map<String, List<Map<String, Long>>> tradeHolidays = new HashMap<>();

    public void initData() throws IOException, ParseException {
        System.out.println("init persistance");
        ArrayList<String> symbols = new ArrayList<>(
                Arrays.asList("GOOGL", "AAPL", "TSLA", "GME", "AMC"));


        //CREATE STOCKS
        for (String symbol: symbols) {
            JsonObject response = apiCall.getResult(apiStringProvider.provideAPIStringForStock(symbol));
            //System.out.println("stock result: "+response_AAPL);
            Stock stock = Stock.builder()
                    .exchange(response.getAsJsonPrimitive("exchange").getAsString())
                    .logo(response.getAsJsonPrimitive("logo").getAsString())
                    .ipo(new SimpleDateFormat("yyyy-MM-dd").parse(response.getAsJsonPrimitive("ipo").getAsString()))
                    .symbol(response.getAsJsonPrimitive("ticker").getAsString())
                    .name(response.getAsJsonPrimitive("name").getAsString())
                    .weburl(response.getAsJsonPrimitive("weburl").getAsString())
                    .sharesOutstanding(NumberRounder.roundFloat(response.getAsJsonPrimitive("shareOutstanding").getAsFloat(),2))
                    .country(response.getAsJsonPrimitive("country").getAsString())
                    .industry(response.getAsJsonPrimitive("finnhubIndustry").getAsString())
                    .build();
            stockRepository.save(stock);
        }

        Stock gameStop = stockRepository.findBySymbol("GME");
        gameStop.setLogo(stockLogoProvider.provideStockLogo("gamestop.com"));
        stockRepository.saveAndFlush(gameStop);

        Stock apple = stockRepository.findBySymbol("AAPL");
        apple.setLogo(stockLogoProvider.provideStockLogo("apple.com"));
        stockRepository.saveAndFlush(apple);

        Stock tesla = stockRepository.findBySymbol("TSLA");
        tesla.setLogo(stockLogoProvider.provideStockLogo("tesla.com"));
        stockRepository.saveAndFlush(tesla);

        Stock AMC = stockRepository.findBySymbol("AMC");
        AMC.setLogo(stockLogoProvider.provideStockLogo("amctheatres.com"));
        stockRepository.saveAndFlush(AMC);


        Stock google = stockRepository.findBySymbol("GOOGL");
        google.setLogo("https://champton.com/wp-content/uploads/alphabet-google-logo-1160x665.jpg");
        stockRepository.saveAndFlush(google);


        //INIT HOLIDAYS CONTAINER

        List<Map<String, Long>> holiday2020_11_26 = new ArrayList<>(2);
        Map<String, Long> holiday2020_11_26_start_end = new HashMap<>();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2020,10,26,15,31,0);
        holiday2020_11_26_start_end.put("start", startDate.getTimeInMillis());
        Calendar endDate = Calendar.getInstance();
        endDate.set(2020,10,27,15,30,59);
        holiday2020_11_26_start_end.put("end", endDate.getTimeInMillis());

        Map<String, Long> UTCTimeStamps = new HashMap<>();
        Calendar fromDate = Calendar.getInstance();
        fromDate.set(2020,10,25,15,30,0);
        UTCTimeStamps.put("from", fromDate.getTimeInMillis()/1000);
        Calendar toDate = Calendar.getInstance();
        toDate.set(2020,10,25,22,0, 0);
        UTCTimeStamps.put("to", toDate.getTimeInMillis()/1000);

        holiday2020_11_26.add(0, holiday2020_11_26_start_end);
        holiday2020_11_26.add(1,UTCTimeStamps);

        tradeHolidays.put("2020_11_26", holiday2020_11_26);


        List<Map<String, Long>> holiday2021_01_18 = new ArrayList<>(2);
        Map<String, Long> holiday2021_01_18_start_end = new HashMap<>();
        Calendar startDate2021_01_18 = Calendar.getInstance();
        startDate2021_01_18.set(2021,0,18,15,31,0);
        holiday2021_01_18_start_end.put("start", startDate2021_01_18.getTimeInMillis());
        Calendar endDate2021_01_18 = Calendar.getInstance();
        endDate2021_01_18.set(2021,0,19,15,30,59);
        holiday2021_01_18_start_end.put("end", endDate2021_01_18.getTimeInMillis());

        Map<String, Long> UTCTimeStamps2021_01_18 = new HashMap<>();
        Calendar fromDate2021_01_18 = Calendar.getInstance();
        fromDate2021_01_18.set(2021,0,15,15,30,0);
        UTCTimeStamps2021_01_18.put("from", fromDate2021_01_18.getTimeInMillis()/1000);
        Calendar toDate2021_01_18 = Calendar.getInstance();
        toDate2021_01_18.set(2021,0,15,22,0, 0);
        UTCTimeStamps2021_01_18.put("to", toDate2021_01_18.getTimeInMillis()/1000);

        holiday2021_01_18.add(0, holiday2021_01_18_start_end);
        holiday2021_01_18.add(1,UTCTimeStamps2021_01_18);

        tradeHolidays.put("2020_01_18", holiday2021_01_18);



        List<Map<String, Long>> holiday2021_02_15 = new ArrayList<>(2);
        Map<String, Long> holiday2021_02_15_start_end = new HashMap<>();
        Calendar startDate2021_02_15 = Calendar.getInstance();
        startDate2021_02_15.set(2021,1,15,15,31,0);
        holiday2021_02_15_start_end.put("start", startDate2021_02_15.getTimeInMillis());
        Calendar endDate2021_02_15 = Calendar.getInstance();
        endDate2021_02_15.set(2021,1,16,15,30,59);
        holiday2021_02_15_start_end.put("end", endDate2021_02_15.getTimeInMillis());

        Map<String, Long> UTCTimeStamps2021_02_15 = new HashMap<>();
        Calendar fromDate2021_02_15 = Calendar.getInstance();
        fromDate2021_02_15.set(2021,1,12,15,30,0);
        UTCTimeStamps2021_01_18.put("from", fromDate2021_01_18.getTimeInMillis()/1000);
        Calendar toDate2021_02_15 = Calendar.getInstance();
        toDate2021_02_15.set(2021,1,12,22,0, 0);
        UTCTimeStamps2021_02_15.put("to", toDate2021_02_15.getTimeInMillis()/1000);

        holiday2021_02_15.add(0, holiday2021_02_15_start_end);
        holiday2021_02_15.add(1,UTCTimeStamps2021_02_15);

        tradeHolidays.put("2020_02_15", holiday2021_02_15);
        System.out.println(tradeHolidays.toString());

        // FORCE RUN PRICE DATA UPDATER
        priceDataUpdater.updateCandlesDay();
        priceDataUpdater.updateCandlesMin1();
        priceDataUpdater.updateCandlesMin5();
        priceDataUpdater.updateLastPrices();


    }
}
