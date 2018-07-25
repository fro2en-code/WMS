package com.ymt.utils;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("messageSender")
public class MessageSender {
    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);
    @Resource(name = "amqpTemplate")
    private AmqpTemplate amqpTemplate;
    @Value("${exchange}")
    private String exchange;

    public void sendMessage(String key, Object message) {
        try {
            amqpTemplate.convertAndSend(exchange, key, message);
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}
