package com.ymt.api;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

public class AccountInitActionTest extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(AccountInitActionTest.class);

    @Test
    public void saveTest() throws Exception {
        // try {
        // mockMvc.perform(MockMvcRequestBuilders.post("/accountInit/save.action"))
        // .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());
        // } catch (Exception e) {
        // logger.error("", e);
        // }
        mockMvc.perform(MockMvcRequestBuilders.post("/login").param("loginname", "404").param("pwd", "1234")
                .param("code", "1234")).andDo(MockMvcResultHandlers.print());
        System.out.println("=================================================");
        for (int i = 0; i < 1; i++) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    for (int j = 0; j < 1; j++) {
                        try {
                            mockMvc.perform(MockMvcRequestBuilders.post("/accountInit/save.action"))
                                    .andDo(MockMvcResultHandlers.print());
                        } catch (Exception e) {
                            logger.error("", e);
                        }
                    }
                }
            };
            t.start();
        }
        Thread.sleep(100000);
    }

}
