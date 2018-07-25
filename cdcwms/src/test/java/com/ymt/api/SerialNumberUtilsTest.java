package com.ymt.api;

import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ymt.utils.SerialNumberUtils;

public class SerialNumberUtilsTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(SerialNumberUtilsTest.class);
    private SerialNumberUtils utils = new SerialNumberUtils() {

        @Override
        public boolean isExpired(Date startTime) {
            return new Date().getTime() / (1000L * 60 * 60 * 24) == startTime.getTime() / (1000L * 60 * 60 * 24);// 是否为同一天
        }
    };

    @Test
    public void getSerialNumberTest() throws InterruptedException {
        // logger.error(utils.getSerialNumber(10) + "==================");
        for (int i = 0; i < 20; i++) {
            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    logger.error(utils.getSerialNumber(10) + "==================" + Thread.currentThread().getName());
                    try {
                        Thread.sleep((int) (Math.random() * 1000));
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    logger.error(utils.getSerialNumber(10) + "==================" + Thread.currentThread().getName());
                }
            });
            t.start();
        }
        Thread.sleep(100000);
    }

}
