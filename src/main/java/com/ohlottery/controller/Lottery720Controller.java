package com.ohlottery.controller;

import com.ohlottery.dto.Lottery720Dto;
import com.ohlottery.entity.Lottery720Entity;
import com.ohlottery.service.LotteryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("lottery720")
@RequiredArgsConstructor
public class Lottery720Controller {

    private final LotteryService lotteryService;

    @GetMapping("/{round}")
    public ResponseEntity<?> getLotteryNumber(@PathVariable("round") Long requestRound) {
        if(requestRound == null) return ResponseEntity.badRequest().build();

        Optional<Lottery720Entity> roundResult
                = lotteryService.getLottery720Result(requestRound);

        if (roundResult.isEmpty()) return ResponseEntity.badRequest().build();

        Lottery720Entity resultEntity = roundResult.get();
        Lottery720Dto resultDto = new Lottery720Dto(resultEntity);
        return ResponseEntity.ok(resultDto);
    }

}
