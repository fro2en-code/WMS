package com.ymt.api;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import wms.warehouse.service.GoodsService;

public class HibernateTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(HibernateTest.class);
    @Autowired
    private GoodsService service;

    @Test
    public void test() {
    }

}
