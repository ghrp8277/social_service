package com.example.socialservice.service;

import com.example.socialservice.constants.KafkaConstants;
import com.example.socialservice.constants.NaverSymbolConstants;
import com.example.socialservice.entity.PostStock;
import com.example.socialservice.repository.PostStockRepository;
import com.example.socialservice.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class KafkaConsumerService {
    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private PostStockRepository postStockRepository;

    @KafkaListener(topics = KafkaConstants.SOCIAL_STOCK_RESPONSE_TOPIC, groupId = KafkaConstants.SOCIAL_STOCK_GROUP_ID)
    public void consumeSocialStockRequest(String message) {
        List<Map<String, String>> stocks = jsonUtil.parseJsonToMapList(message);
        List<PostStock> postStocksToSave = new ArrayList<>();

        stocks.forEach(stock -> {
            String stockCode = stock.get("code");
            String stockName = stock.get("name");

            Optional<PostStock> postStockOptional = postStockRepository.findByStockCodeAndStockName(stockCode, stockName);
            if (postStockOptional.isEmpty()) {
                PostStock postStock = new PostStock();
                postStock.setStockCode(stockCode);
                postStock.setStockName(stockName);
                postStock.setMarket(NaverSymbolConstants.Market.KOSPI);
                postStocksToSave.add(postStock);
            }
        });

        if (!postStocksToSave.isEmpty()) {
            postStockRepository.saveAll(postStocksToSave);
        }
    }
}
