package com.ohlottery.dto;

import com.ohlottery.entity.Lottery645Entity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "6/45 로또 정보")
public class Lottery645Dto {

    public Lottery645Dto(Lottery645Entity entity) {
        round = entity.getRound();
        drawDate = entity.getDrawDate(); // 마이그레이션 해줌
        drawNo1 = entity.getDrawNo1();
        drawNo2 = entity.getDrawNo2();
        drawNo3 = entity.getDrawNo3();
        drawNo4 = entity.getDrawNo4();
        drawNo5 = entity.getDrawNo5();
        drawNo6 = entity.getDrawNo6();
        bonusNo = entity.getBonusNo();
        firstAccumulateAmount = entity.getFirstAccumulateAmount();
        firstPrizeWinnerCount = entity.getFirstPrizeWinnerCount();
        totalSellAmount = entity.getTotalSellAmount();
    }

    @Schema(description = "회차 정보", example = "1148")
    private long round;

    @Schema(description = "추첨 날짜", example = "2024-11-30")
    private LocalDate drawDate;

    @Schema(description = "첫번째 번호", example = "3")
    private int drawNo1;
    @Schema(description = "두번째 번호", example = "6")
    private int drawNo2;
    @Schema(description = "세번째 번호", example = "13")
    private int drawNo3;
    @Schema(description = "네번째 번호", example = "15")
    private int drawNo4;
    @Schema(description = "다섯번째 번호", example = "16")
    private int drawNo5;
    @Schema(description = "여섯번째 번호", example = "22")
    private int drawNo6;
    @Schema(description = "보너스 번호", example = "32")
    private int bonusNo;

    @Schema(description = "1등 총 당첨금액 (1등 당첨자 수 * 1등 당첨금 한명분 금액)", example = "26953320004")
    private long firstAccumulateAmount;

    @Schema(description = "1등 당첨자 수", example = "13")
    private long firstPrizeWinnerCount;

    @Schema(description = "1등 당첨금 한명분 금액 (세전)", example = "2073332308")
    private long firstWinAmount;

    @Schema(description = " 판매금액", example = "113188640000")
    private long totalSellAmount;
}
