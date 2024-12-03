package com.ohlottery.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

@Entity
@Getter
public class Lottery720Entity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long round;// 회차

    @Temporal(TemporalType.DATE)
    private Date drawDate;// 추첨 날짜

    private short rankWinNum;// 등수
    private byte rankClass;// 조 번호
    private int rankNo;// 당첨 번호
}