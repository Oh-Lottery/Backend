package com.ohlottery.controller;

import com.ohlottery.dto.Lottery645Dto;
import com.ohlottery.dto.Lottery720Dto;
import com.ohlottery.service.LotteryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lottery/ai")
public class LotteryAIController {

    private final LotteryService lotteryService;

    @GetMapping("/predict/645/{round}")
    public ResponseEntity<Lottery645Dto> get645Predict(
            @PathVariable long round
    ) {
        return ResponseEntity.ok(
                new Lottery645Dto(
                        0,
                        null,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0
                )
        );
    }

    @GetMapping("/statistic/645")
    public ResponseEntity<?> get645Statistic() {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/trend/645")
    public ResponseEntity<?> get645Trend() {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/predict/720/{round}")
    public ResponseEntity<Lottery720Dto> get720Predict(
            @PathVariable long round
    ) {
        return ResponseEntity.ok(
                new Lottery720Dto(
                        0,
                        null,
                        0,
                        0,
                        0
                )
        );
    }

    @GetMapping("/statistic/720")
    public ResponseEntity<?> get720Statistic() {
        return ResponseEntity.ok(null);
    }

    @GetMapping("/trend/720")
    public ResponseEntity<?> get720Trend() {
        return ResponseEntity.ok(null);
    }

}
