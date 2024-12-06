package com.ohlottery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OhLotteryApplication {

    public static void main(String[] args) {
        SpringApplication.run(OhLotteryApplication.class, args);
    }

}
