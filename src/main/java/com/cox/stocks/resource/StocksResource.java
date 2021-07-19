package com.cox.stocks.resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cox.stocks.data.WatchedStocks;
import com.cox.stocks.service.StocksService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@RestController
@RequestMapping(value="resource/stocks")

public class StocksResource
{
    @Autowired
    StocksService stocksService;

    Set<String> watchedStocksSet = new HashSet<String>();


    @ApiOperation(value="Retrieve stock information.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Integer.class),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value = {"retrieve/{stockValue}"}, method = RequestMethod.GET)
    public ResponseEntity<Stock> retrieveStock(@PathVariable("stockValue") String stockValue)
    {//curl http://localhost:8080/resource/stocks/retrieve/ANGI
        Stock stock;
        try
        {
            stock = YahooFinance.get(stockValue);
        }
        catch(IOException ex)
        {
            return null;//todo?
        }

        return ResponseEntity.status(HttpStatus.OK).body(stock);
    }

    @ApiOperation(value="Add stock to watched list.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Integer.class),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value = {"add/watched/{stockValue}"}, method = RequestMethod.PUT)
    public ResponseEntity<String> addWatchedStock(@PathVariable("stockValue") String stockValue)
    {//curl http://localhost:8080/resource/stocks/add/watched/ANGI
        if(watchedStocksSet.add(stockValue) == false)
        {
            return ResponseEntity.status(HttpStatus.OK).body("Stock already being watched");

        }
        return ResponseEntity.status(HttpStatus.OK).body("Stock added to watched stock list");
    }

    @ApiOperation(value="Remove stock from watched list.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Integer.class),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value = {"remove/watched/{stockValue}"}, method = RequestMethod.PUT)
    public ResponseEntity<String> removeWatchedStock(@PathVariable("stockValue") String stockValue)
    {//curl http://localhost:8080/resource/stocks/remove/watched/ANGI
        if(watchedStocksSet.remove(stockValue) == false)
        {
            return ResponseEntity.status(HttpStatus.OK).body("Stock was not in the list");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Stock removed from the watched stock list");
    }
}