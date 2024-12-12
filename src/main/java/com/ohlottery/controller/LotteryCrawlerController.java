package com.ohlottery.controller;

import com.ohlottery.service.DHLotteryCrawlerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//Authentication 과정 필수로 필요

@RestController
@RequestMapping("/crawler")
@RequiredArgsConstructor
@Tag(name = "Lottery Crawler Controller", description = "당첨 정보 수동 크롤링")
public class LotteryCrawlerController {

    private final DHLotteryCrawlerService lotteryCrawlerService;

    @Operation(summary = "6/45로또 데이터 업데이트", description = "해당 회차 6/45 당첨 정보를 데이터베이스에 저장")
    @GetMapping("/lottery645/{round}")
    public ResponseEntity<Void> fetchLottery645(
            @Parameter(description = "회차", example = "1")
            @PathVariable long round
    ) {
        lotteryCrawlerService.fetchLottery645Data(round);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "연금복권 데이터 업데이트", description = "해당 회차 6/45 당첨 정보를 데이터베이스에 저장")
    @GetMapping("/lottery720/{round}")
    public ResponseEntity<Void> fetchLottery720(
            @Parameter(description = "회차", example = "1")
            @PathVariable long round
    ) {
        lotteryCrawlerService.fetchLottery720Data(round);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "모든 로또 데이터 업데이트", description = "1~n 회차까지 전부 데이터베이스에 저장")
    @GetMapping("/lottery/fetch")
    public ResponseEntity<Void> fetchAllData() {
        lotteryCrawlerService.fetchAllLotteryData();
        return ResponseEntity.ok().build();
    }
}
