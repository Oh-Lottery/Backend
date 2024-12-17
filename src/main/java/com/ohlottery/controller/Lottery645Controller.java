package com.ohlottery.controller;

import com.ohlottery.dto.Lottery645Dto;
import com.ohlottery.entity.Lottery645Entity;
import com.ohlottery.service.LotteryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Lottery 6/45 Controller", description = "6/45 당첨 정보 API")
public class Lottery645Controller {

    private final LotteryService lotteryService;

    @GetMapping("/round/{round}")

    @Operation(summary = "6/45로또 데이터 조회", description = "해당 회차 6/45 당첨 정보를 데이터베이스에서 조회")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "데이터 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Lottery645Dto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 인자 값", content = @Content()),
            @ApiResponse(responseCode = "404", description = "해당 회차 당첨 정보가 없음", content = @Content())
    })
    public ResponseEntity<?> getLotteryNumber(
            @Parameter(description = "회차", example = "1")
            @PathVariable("round") String round
    ) {
        long requestRound;
        try {
            requestRound = Long.parseLong(round);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Lottery645Entity> roundResult
                = lotteryService.getLottery645Result(requestRound);

        if (roundResult.isEmpty()) return ResponseEntity.notFound().build();

        Lottery645Entity resultEntity = roundResult.get();
        Lottery645Dto resultDto = new Lottery645Dto(
                resultEntity.getRound(),
                resultEntity.getDrawDate(),
                resultEntity.getDrawNo1(),
                resultEntity.getDrawNo2(),
                resultEntity.getDrawNo3(),
                resultEntity.getDrawNo4(),
                resultEntity.getDrawNo5(),
                resultEntity.getDrawNo6(),
                resultEntity.getBonusNo(),
                resultEntity.getFirstAccumulateAmount(),
                resultEntity.getFirstPrizeWinnerCount(),
                resultEntity.getFirstWinAmount(),
                resultEntity.getTotalSellAmount()
        );
        return ResponseEntity.ok(resultDto);
    }
}