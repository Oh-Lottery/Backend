package com.ohlottery.dto;

import com.ohlottery.entity.Lottery645Entity;
import lombok.Data;

import java.util.Date;

@Data
public class Lottery645Dto {

    public Lottery645Dto(Lottery645Entity entity){
        round = entity.getRound();
        drawDate = entity.getDrawDate();
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

    private long round;

    private Date drawDate;

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
}
