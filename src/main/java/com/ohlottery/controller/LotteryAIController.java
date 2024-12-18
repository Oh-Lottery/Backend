package com.ohlottery.controller;

import com.ohlottery.service.LotteryAINumberService;
import com.ohlottery.service.LotteryAIPriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lottery/ai")
public class LotteryAIController {

    private final LotteryAIPriceService lotteryAIPriceService;
    private final LotteryAINumberService lotteryAINumberService;

    @GetMapping("/predict/prize/645")

    @Operation(summary = "6/45 로또 상금 예측", description = "다음 1등 상금 예측 결과 제공")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 조회 성공", content = @Content()),
            @ApiResponse(responseCode = "400", description = "잘못된 인자 값", content = @Content()),
            @ApiResponse(responseCode = "404", description = "해당 회차 당첨 정보가 없음", content = @Content())
    })
    public ResponseEntity<String> getPrediction() {
        try {
            String predictionResult = lotteryAIPriceService.executePythonPredictionScript();
            return ResponseEntity.ok(predictionResult);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Prediction error: " + e.getMessage());
        }
    }

    @GetMapping("/predict/num/645")

    @Operation(summary = "6/45 로또 번호 예측", description = "다음 1등 번호 예측 결과 제공")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "데이터 조회 성공", content = @Content()),
            @ApiResponse(responseCode = "400", description = "잘못된 인자 값", content = @Content()),
            @ApiResponse(responseCode = "404", description = "해당 회차 당첨 정보가 없음", content = @Content())
    })
    public ResponseEntity<?> getLotteryPredictions() {
        return ResponseEntity.ok(lotteryAINumberService.getAINumberResults().toString());
    }

}
