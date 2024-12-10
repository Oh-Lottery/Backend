package com.ohlottery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Schema(description = "연금복권 정보")
public class Lottery720Dto {

    @Schema(description = "회차 정보", example = "240")
    private long round;

    @Schema(description = "추첨 날짜", example = "2024-12-05")
    private LocalDate drawDate;

    @Schema(description = "등수", example = "1")
    private int rankWinNum;

    @Schema(description = "당첨 조 번호", example = "4")
    private int rankClass;

    @Schema(description = "당첨 번호", example = "446648")
    private int rankNo;
}
