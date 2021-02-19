package com.codecool.stocktrader.controller;

import com.codecool.stocktrader.model.*;
import com.codecool.stocktrader.repository.CandleRepository;
import com.codecool.stocktrader.repository.LastPriceRepository;
import com.codecool.stocktrader.repository.StockRepository;
import com.codecool.stocktrader.service.NumberRounder;
import com.codecool.stocktrader.service.ReactCandleDataConverter;
import com.codecool.stocktrader.service.ResolutionProvider;
import com.codecool.stocktrader.service.StockListDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private CandleRepository candleRepository;

    @Autowired
    private ResolutionProvider resolutionProvider;

    @Autowired
    private LastPriceRepository lastPriceRepository;

    @Autowired
    private ReactCandleDataConverter reactCandleDataConverter;

    @Autowired
    private StockListDataProvider stockListDataProvider;

    @GetMapping("/getcandle/{symbol}/{resolution}")
    public ReactCandleContainer returnIntradayPerRes(@PathVariable("symbol") String symbol, @PathVariable("resolution") String res) {
        Stock stock = stockRepository.findBySymbol(symbol);
        Resolution resolution = resolutionProvider.createResolution(res);
        CandleContainer candleReturn = candleRepository.findAllByStockAndResolution(stock, resolution);
        ReactCandleContainer reactCandleContainer = new ReactCandleContainer();
        reactCandleDataConverter.convertCandleData(candleReturn, resolution, reactCandleContainer);

        return reactCandleContainer;

    }

    //USED
    @GetMapping("/getcandle/{symbol}")
    public ReactCandleContainer returnIntraday(@PathVariable("symbol") String symbol) {
        Stock stock = stockRepository.findBySymbol(symbol);

        Resolution resolution1 = resolutionProvider.createResolution("1");
        Resolution resolution5 = resolutionProvider.createResolution("5");
        Resolution resolutionD = resolutionProvider.createResolution("D");

        CandleContainer candleReturn1 = candleRepository.findAllByStockAndResolution(stock, resolution1);
        CandleContainer candleReturn5 = candleRepository.findAllByStockAndResolution(stock, resolution5);
        CandleContainer candleReturnD = candleRepository.findAllByStockAndResolution(stock, resolutionD);

        ReactCandleContainer reactCandleContainer = new ReactCandleContainer();

        reactCandleDataConverter.convertCandleData(candleReturn1, resolution1, reactCandleContainer);
        reactCandleDataConverter.convertCandleData(candleReturn5, resolution5, reactCandleContainer);
        reactCandleDataConverter.convertCandleData(candleReturnD, resolutionD, reactCandleContainer);

        return reactCandleContainer;

    }

    //USED
    @GetMapping("/getquote/{symbol}")
    public LastPrice returnCurrentPrice(@PathVariable("symbol") String symbol) {
        System.out.println("GETQUOTE REQUESTED");
        Stock stock = stockRepository.findBySymbol(symbol);
        return lastPriceRepository.findByStock(stock);
    }

    //USED
    @GetMapping("/getstock/{symbol}")
    public Stock returnStock(@PathVariable("symbol") String symbol) {
        Stock stock = stockRepository.findBySymbol(symbol);
        return stock;
    }

    //USED by userAccount
    @GetMapping("/getallstocks/")
    public StockList returnAllStock() {
        StockList stockList = new StockList();
        List<Stock> stocks = stockRepository.findAll();
        for (Stock stock:stocks) {
            stockList.getStocks().add(stock);
        }
        return stockList;

    }

    @GetMapping("/getAllLastPrice")
    public  List<LastPrice> returnAllLastPrice(){
        return lastPriceRepository.findAll();
    }

    //USED
    @GetMapping("/getStockListData")
    public  List<StockListData> returnStockListData(){
        return stockListDataProvider.provideStockListData();
    }

    //USED
    @GetMapping("/getStockSymbols")
    public  List<String> returnStockNameList(){
        return stockListDataProvider.provideStockNameList();
    }


}
