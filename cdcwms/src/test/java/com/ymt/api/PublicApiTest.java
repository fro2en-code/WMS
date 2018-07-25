package com.ymt.api;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import wms.business.service.WmsStockService;

public class PublicApiTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(PublicApiTest.class);
    @Autowired
    private PublicApi api;
    @Autowired
    private WmsStockService service;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @SuppressWarnings("unchecked")
    @Test
    public void testGetTask() {
        final String key = "key";
        final String value = "value";
        redisTemplate.execute(new RedisCallback<Boolean>() {

            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                connection.setNX(key.getBytes(), value.getBytes());
                connection.setEx(key.getBytes(), 1000, value.getBytes());
                return null;
            }
        });
    }
}