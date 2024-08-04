package com.example.socialservice.config;

import com.example.socialservice.constants.NaverSymbolConstants;
import com.example.socialservice.entity.PostStock;
import com.example.socialservice.repository.PostStockRepository;
import com.example.socialservice.service.KafkaProducerService;
import com.example.socialservice.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Configuration
public class DataInitializationConfig {

    @Autowired
    private PostStockRepository postStockRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private JsonUtil jsonUtil;

    @PostConstruct
    public void initData() {
        initializePostStocks();
    }

    private void initializePostStocks() {
        sendStockData(NaverSymbolConstants.Market.KOSPI);
        sendStockData(NaverSymbolConstants.Market.KOSDAQ);
    }

    private void sendStockData(String market) {
        Map<String, String> map = new HashMap<>();
        map.put("market", market);
        String message = jsonUtil.toJson(map);
        kafkaProducerService.sendStockMessage(message);
    }
}
