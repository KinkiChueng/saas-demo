package com.example;

import com.example.saasdemo.SaasDemoFirstApplication;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zhangjinqi
 * @date 2023年07月26日 17:21
 */
@SpringBootTest(classes = SaasDemoFirstApplication.class)
public class TransTest {
    @GlobalTransactional
    public void test3() {

        int i = 1/0; // 异常
    }
}
