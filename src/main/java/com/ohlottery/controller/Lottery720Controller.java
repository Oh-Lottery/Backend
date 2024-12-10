package com.ohlottery.controller;

import com.ohlottery.dto.Lottery720Dto;
import com.ohlottery.entity.Lottery720Entity;
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
@RequestMapping("lottery720/round")
@RequiredArgsConstructor

@Tag(name = "Lottery 720 Controller", description = "연금복권 당첨 정보 API")
public class Lottery720Controller {

    private final LotteryService lotteryService;

    @Operation(summary = "연금복권 데이터 조회", description = "해당 회차 연금복권 당첨 정보를 데이터베이스에서 조회")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "데이터 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Lottery720Dto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 인자 값", content = @Content()),
            @ApiResponse(responseCode = "404", description = "해당 회차 당첨 정보가 없음", content = @Content())
    })
    @GetMapping("/{round}")
    public ResponseEntity<?> getLotteryNumber(
            @Parameter(description = "회차", example = "1")
            @PathVariable("round") Long requestRound
    ) {
        if(requestRound == null) return ResponseEntity.badRequest().build();

        Optional<Lottery720Entity> roundResult
                = lotteryService.getLottery720Result(requestRound);

        if (roundResult.isEmpty()) return ResponseEntity.notFound().build();

        Lottery720Entity resultEntity = roundResult.get();
        Lottery720Dto resultDto = new Lottery720Dto(
                resultEntity.getRound(),
                resultEntity.getDrawDate(),
                resultEntity.getRankWinNum(),
                resultEntity.getRankClass(),
                resultEntity.getRankNo()
        );
        return ResponseEntity.ok(resultDto);
    }

}
