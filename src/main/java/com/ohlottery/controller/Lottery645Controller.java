package com.ohlottery.controller;

import com.ohlottery.service.LotteryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("lottery645")
@RequiredArgsConstructor
public class Lottery645Controller {

    private final LotteryService lotteryService;

    @GetMapping("/{round}")
    public void getLotteryNumber(@PathVariable int round) {

    }
}