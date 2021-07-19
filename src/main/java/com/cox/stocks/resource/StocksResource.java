package com.cox.stocks.resource;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cox.stocks.data.WatchedStock;
import com.cox.stocks.data.WatchedStockHistorical;
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
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

@RestController
@RequestMapping(value="resource/stocks")

public class StocksResource
{
    @Autowired
    StocksService stocksService;

    Set<String> watchedStocksSymbolSet = new HashSet<String>();


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
    {//curl -X PUT http://localhost:8080/resource/stocks/add/watched/YELP
        if(watchedStocksSymbolSet.add(stockValue) == false)
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
    {//curl http://localhost:8080/resource/stocks/remove/watched/YELP
        if(watchedStocksSymbolSet.remove(stockValue) == false)
        {
            return ResponseEntity.status(HttpStatus.OK).body("Stock was not in the list");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Stock removed from the watched stock list");
    }


    @ApiOperation(value="Retrieve current watched stock information.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Integer.class),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value = {"view/watched/stocks/current"}, method = RequestMethod.GET)
    public ResponseEntity<List<WatchedStock>> viewWatchedStocksCurrent()
    {//curl http://localhost:8080/resource/stocks/view/watched/stocks/current

        List<WatchedStock> watchedStocks = stocksService.retrieveCurrentStockData(watchedStocksSymbolSet);

        return ResponseEntity.status(HttpStatus.OK).body(watchedStocks);
    }

    @ApiOperation(value="Retrieve historical watched stock information.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Integer.class),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value = {"view/watched/stocks/historical"}, method = RequestMethod.GET)
    public ResponseEntity<List<WatchedStockHistorical>> viewWatchedStocksHistorical()
    {//curl http://localhost:8080/resource/stocks/view/watched/stocks/historical

        List<WatchedStockHistorical> watchedStocks = stocksService.retrieveHistoricalStockData(watchedStocksSymbolSet);

        return ResponseEntity.status(HttpStatus.OK).body(watchedStocks);
    }



    @ApiOperation(value="Retrieve historical watched stock information.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Integer.class),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value = {"temp"}, method = RequestMethod.GET)
    public ResponseEntity<List<HistoricalQuote>> temp() throws IOException {


        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.YEAR, -1); // from 5 years ago

        Stock google = YahooFinance.get("GOOG", from, to, Interval.DAILY);
        List<HistoricalQuote> x = google.getHistory();

        //Stock tesla = YahooFinance.get("TSLA", true);

        return ResponseEntity.status(HttpStatus.OK).body(google.getHistory());
    }
}