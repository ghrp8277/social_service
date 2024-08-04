package com.example.socialservice.service;

import com.example.socialservice.constants.KafkaConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendStockMessage(String message) {
        kafkaTemplate.send(KafkaConstants.SOCIAL_STOCK_REQUEST_TOPIC, message);
    }
}
