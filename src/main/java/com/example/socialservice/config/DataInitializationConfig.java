package com.example.socialservice.config;

import com.example.socialservice.constants.NaverSymbolConstants;
import com.example.socialservice.entity.PostStock;
import com.example.socialservice.repository.PostStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Configuration
public class DataInitializationConfig {

    @Autowired
    private PostStockRepository postStockRepository;

    @PostConstruct
    public void initData() {
        initializePostStocks();
    }

    private void initializePostStocks() {
        NaverSymbolConstants.KOSPI.SYMBOLS.forEach((stockCode, stockName) ->
                createPostStockIfNotExists(stockCode, stockName, NaverSymbolConstants.Market.KOSPI)
        );
        NaverSymbolConstants.KOSDAQ.SYMBOLS.forEach((stockCode, stockName) ->
                createPostStockIfNotExists(stockCode, stockName, NaverSymbolConstants.Market.KOSDAQ)
        );
    }

    private void createPostStockIfNotExists(Long stockCode, String stockName, String market) {
        Optional<PostStock> postStockOptional = postStockRepository.findByStockCodeAndStockName(stockCode, stockName);
        if (postStockOptional.isEmpty()) {
            PostStock postStock = new PostStock();
            postStock.setStockCode(stockCode);
            postStock.setStockName(stockName);
            postStock.setMarket(market);
            postStockRepository.save(postStock);
        }
    }
}
