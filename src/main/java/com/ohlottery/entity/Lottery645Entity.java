package com.ohlottery.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lottery645Entity {

    @Id // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long round;

    private LocalDate drawDate;

    private byte drawNo1;
    private byte drawNo2;
    private byte drawNo3;
    private byte drawNo4;
    private byte drawNo5;
    private byte drawNo6;
    private byte bonusNo;
    // 1등 총 당첨금액 (1등 당첨자 수 * 1등 당첨금 한명분 금액)
    private long firstAccumulateAmount;
    // 1등 당첨자 수
    private long firstPrizeWinnerCount;
    // 1등 당첨금 한명분 금액 (세전)
    private long firstWinAmount;
    // 총 판매금액
    private long totalSellAmount;

    @Builder
    public Lottery645Entity(long round, LocalDate drawDate, byte drawNo1, byte drawNo2, byte drawNo3, byte drawNo4,
                            byte drawNo5, byte drawNo6, byte bonusNo, long firstAccumulateAmount,
                            long firstPrizeWinnerCount, long firstWinAmount, long totalSellAmount) {
        this.round = round;
        this.drawDate = drawDate;
        this.drawNo1 = drawNo1;
        this.drawNo2 = drawNo2;
        this.drawNo3 = drawNo3;
        this.drawNo4 = drawNo4;
        this.drawNo5 = drawNo5;
        this.drawNo6 = drawNo6;
        this.bonusNo = bonusNo;
        this.firstAccumulateAmount = firstAccumulateAmount;
        this.firstPrizeWinnerCount = firstPrizeWinnerCount;
        this.firstWinAmount = firstWinAmount;
        this.totalSellAmount = totalSellAmount;
    }
}
