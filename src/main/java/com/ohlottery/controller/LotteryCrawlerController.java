package com.ohlottery.controller;

import com.ohlottery.service.DHLotteryCrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//Authentication 과정 필수로 필요

@RestController
@RequestMapping("/crawler")
@RequiredArgsConstructor
public class LotteryCrawlerController {

    private final DHLotteryCrawlerService lotteryCrawlerService;

    @GetMapping("/lottery645/{round}")
    public ResponseEntity<Void> fetchLottery645(@PathVariable long round) {
        lotteryCrawlerService.fetchLottery645Data(round);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/lottery720/{round}")
    public ResponseEntity<Void> fetchLottery720(@PathVariable long round) {
        lotteryCrawlerService.fetchLottery720Data(round);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/lottery/fetch")
    public ResponseEntity<Void> fetchAllData() {
        lotteryCrawlerService.fetchAllLotteryData();
        return ResponseEntity.ok().build();
    }
}
