package com.ymt.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.plat.common.utils.Callback;

@Service("lockUtils")
public class LockUtils {
    private static final byte[] defaultValue = new byte[0];
    private static final long maxWaitTime = 1000 * 60 * 5L;// 最大等待5分钟
    private static final long waitTime = 100L;
    private static final String prefix = "WmsLock:";
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * maxLockTime 单位是秒,切记
     */
    public boolean lock(String key, final String value, final long maxLockTime) {
        final String redisKey = prefix + key;
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) {
                long time = System.currentTimeMillis() + maxWaitTime;
                try {
                    do {
                        if (connection.setNX(redisKey.getBytes(), null != value ? value.getBytes() : defaultValue)) {
                            connection.expire(redisKey.getBytes(), maxLockTime);
                            return true;
                        }
                        try {
                            TimeUnit.MILLISECONDS.sleep(waitTime);
                        } catch (InterruptedException e) {
                            throw new RuntimeException("redis lock 异常");
                        }
                    } while (System.currentTimeMillis() < time);
                } finally {
                    connection.close();
                }
                return false;
            }
        });
    }

    public <T> T lock(final String key, final String value, final long maxLockTime, Callback<T> callback) {
        lock(key, value, maxLockTime);
        try {
            T result = callback.run();
            return result;
        } finally {
            releaseLock(key);
        }
    }

    public boolean releaseLock(final String key) {
        final String redisKey = prefix + key;
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                try {
                    connection.del(redisKey.getBytes());
                } finally {
                    connection.close();
                }
                return true;
            }
        });
    }

}
