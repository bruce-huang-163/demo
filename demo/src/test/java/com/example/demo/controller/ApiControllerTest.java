package com.example.demo.controller;

import org.junit.jupiter.api.Test;
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

    @Test
    void testApiRateLimit() throws Exception {
        System.out.println("Test Started");
        
        String userId = "user1";
        int requestCount = 500;
        int totalRequests = requestCount * 60;

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(totalRequests);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < totalRequests; i++) {
            executorService.execute(() -> {
                try {
                    mockMvc.perform(MockMvcRequestBuilders.get("/api/api1")
                           .header("user-id", userId)
                           .contentType(MediaType.APPLICATION_JSON))
                           .andExpect(status().isOk());
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
}