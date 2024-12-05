package com.ohlottery.dto;

import com.ohlottery.entity.Lottery720Entity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "연금복권 정보")
public class Lottery720Dto {

    public Lottery720Dto(Lottery720Entity entity) {
        round = entity.getRound();
        drawDate = entity.getDrawDate();
        rankWinNum = entity.getRankWinNum();
        rankClass = entity.getRankClass();
        rankNo = entity.getRankNo();
    }

    @Schema(description = "회차 정보", example = "240")
    private long round;

    @Schema(description = "추첨 날짜", example = "2024-12-05")
    private Date drawDate;

    @Schema(description = "등수", example = "1")
    private short rankWinNum;

    @Schema(description = "당첨 조 번호", example = "4")
    private int rankClass;

    @Schema(description = "당첨 번호", example = "446648")
    private int rankNo;
}
