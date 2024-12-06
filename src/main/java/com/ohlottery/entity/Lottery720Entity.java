package com.ohlottery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Lottery720Entity {

    // @GeneratedValue(strategy = GenerationType.IDENTITY) 제거
    @Id
    private long round;// 회차

    // @Temporal(TemporalType.DATE) 제거
    private LocalDate drawDate;// 추첨 날짜 // 상범님과 논의 예정

    private short rankWinNum;// 등수
    private byte rankClass;// 조 번호
    private int rankNo;// 당첨 번호
}
