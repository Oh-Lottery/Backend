package com.ohlottery.controller;

import com.ohlottery.dto.Lottery645Dto;
import com.ohlottery.entity.Lottery645Entity;
import com.ohlottery.service.LotteryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("lottery645")
@RequiredArgsConstructor
public class Lottery645Controller {

    private final LotteryService lotteryService;

    @GetMapping("/{round}")
    public ResponseEntity<?> getLotteryNumber(@PathVariable("round") String round) {
        long requestRound;
        try {
            requestRound = Long.parseLong(round);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Lottery645Entity> roundResult
                = lotteryService.getLottery645Result(requestRound);

        if (roundResult.isEmpty()) return ResponseEntity.badRequest().build();

        Lottery645Entity resultEntity = roundResult.get();
        Lottery645Dto resultDto = new Lottery645Dto(resultEntity);
        return ResponseEntity.ok(resultDto);
    }
}