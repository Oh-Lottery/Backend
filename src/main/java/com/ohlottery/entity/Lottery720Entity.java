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
public class Lottery720Entity {

    @Id // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long round;// 회차

    private LocalDate drawDate;// 추첨 날짜

    private short rankWinNum;// 등수
    private byte rankClass;// 조 번호
    private int rankNo;// 당첨 번호

    @Builder
    public Lottery720Entity(long round, LocalDate drawDate, short rankWinNum, byte rankClass, int rankNo) {
        this.round = round;
        this.drawDate = drawDate;
        this.rankWinNum = rankWinNum;
        this.rankClass = rankClass;
        this.rankNo = rankNo;
    }
}
