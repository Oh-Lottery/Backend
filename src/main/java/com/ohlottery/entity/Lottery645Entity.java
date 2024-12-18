package com.ohlottery.entity;

import com.ohlottery.entity.middle.Lottery645EntityStore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Lottery645Entity {

    @Id
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

    @OneToMany(mappedBy = "lotteryEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Lottery645EntityStore> storeList = new ArrayList<>();
}
