package com.ohlottery.controller;

import com.ohlottery.service.DHLotteryCrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Authentication 과정 필수로 필요

@RestController
@RequestMapping("/crawler")
@RequiredArgsConstructor
public class LotteryCrawlerController {

    private final DHLotteryCrawlerService lotteryCrawlerService;

    @PatchMapping("/lottery645/{round}")
    public ResponseEntity<Void> fetchLottery645(@PathVariable long round) {
        lotteryCrawlerService.fetchLottery645Data(round);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/lottery720/{round}")
    public ResponseEntity<Void> fetchLottery720(@PathVariable long round) {
        lotteryCrawlerService.fetchLottery720Data(round);
        return ResponseEntity.ok().build();
    }
}
