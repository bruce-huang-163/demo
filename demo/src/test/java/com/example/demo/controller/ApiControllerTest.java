package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.service.RateLimitService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@WebMvcTest(ApiController.class)
public class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private RateLimitService rateLimitService;

    @Test
    void test() {
        System.out.println("Start Test");
    }

    @ParameterizedTest
    @CsvSource(value = {
        "api1, user1",
        "api2, user2",
        "api3, user3"
    })
    void testApiRateLimit(String apiName, String userId) throws Exception {
        int requestCount = 500;
        int totalRequests = requestCount * 60;

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(totalRequests);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < totalRequests; i++) {
            executorService.execute(() -> {
                try {
                    testApiHelper(apiName, userId);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        long endTime = System.currentTimeMillis();
        System.out.println("Total time taken for requests: " + (endTime - startTime) + " ms");

    }

    void testApiHelper(String apiName, String userId) throws Exception {
        if ("api1".equals(apiName)) {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/api1")
                           .header("user-id", userId)
                           .contentType(MediaType.APPLICATION_JSON))
                           .andExpect(status().isOk());
        } else if ("api2".equals(apiName)) {
            mockMvc.perform(MockMvcRequestBuilders.post("/api/api2")
                           .header("user-id", userId)
                           .contentType(MediaType.APPLICATION_JSON))
                           .andExpect(status().isOk());
        } else {
            mockMvc.perform(MockMvcRequestBuilders.put("/api/api3")
                           .header("user-id", userId)
                           .contentType(MediaType.APPLICATION_JSON))
                           .andExpect(status().isOk());
        }
    }
}