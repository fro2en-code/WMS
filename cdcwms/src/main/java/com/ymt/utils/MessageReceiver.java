package com.ymt.utils;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.stereotype.Component;

@Component("messageReceiver")
public class MessageReceiver implements MessageListener {

    @Override
    public void onMessage(Message message) {
        String contentType = (message.getMessageProperties() != null) ? message.getMessageProperties().getContentType()
                : null;
        if (MessageProperties.CONTENT_TYPE_SERIALIZED_OBJECT.equals(contentType)) {
            Object obj = SerializationUtils.deserialize(message.getBody());
            System.out.println("==================Object:" + obj);
        }
        if (MessageProperties.CONTENT_TYPE_TEXT_PLAIN.equals(contentType)
                || MessageProperties.CONTENT_TYPE_JSON.equals(contentType)
                || MessageProperties.CONTENT_TYPE_JSON_ALT.equals(contentType)
                || MessageProperties.CONTENT_TYPE_XML.equals(contentType)) {
            System.out.println("==================str:" + new String(message.getBody()));
        }
    }

}
