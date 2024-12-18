package com.ohlottery.controller;

import com.ohlottery.dto.Lottery645Dto;
import com.ohlottery.dto.Lottery720Dto;
import com.ohlottery.service.LotteryAINumberService;
import com.ohlottery.service.LotteryAIPriceService;
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

    private final LotteryAIPriceService lotteryAIPriceService;
    private final LotteryAINumberService lotteryAINumberService;

    @GetMapping("/predict-price/645/{round}")
    public ResponseEntity<String> getPrediction() {
        try {
            String predictionResult = lotteryAIPriceService.executePythonPredictionScript();
            return ResponseEntity.ok(predictionResult);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Prediction error: " + e.getMessage());
        }
    }

    @GetMapping("/predict-num/645/{round}")
    public ResponseEntity<?> getLotteryPredictions() {
        return ResponseEntity.ok(lotteryAINumberService.getAINumberResults().toString());
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
        return null;
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
