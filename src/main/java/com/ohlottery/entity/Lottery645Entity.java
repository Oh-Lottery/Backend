package com.ohlottery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter //builder 사용 안하는 대신 추가
@NoArgsConstructor
public class Lottery645Entity {

    // @GeneratedValue(strategy = GenerationType.IDENTITY) 제거 -> 이것도 상범님과 논의
    @Id
    private long round;

    // @Temporal(TemporalType.DATE) 제거 -> 이것도 상범님과 논의
    private LocalDate drawDate; // 상범님과 논의 예정

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
