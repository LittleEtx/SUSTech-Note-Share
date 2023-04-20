package com.example.SUSTechNote.app;

import com.example.SUSTechNote.util.RedisServiceHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestApp {
    private final RedisServiceHelper redisService;

    public TestApp(RedisServiceHelper redisService) {
        this.redisService = redisService;
    }

    @GetMapping("/redis_test")
    public void test() {
        redisService.put("a", "1", 5);
    }

    @GetMapping("/project_test")
    public int projectTest() {
        return 200;
    }
}
