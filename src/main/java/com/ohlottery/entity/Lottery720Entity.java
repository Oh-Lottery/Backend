package com.ohlottery.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Lottery720Entity {

    @Id
    private long round;// 회차

    private LocalDate drawDate;// 추첨 날짜

    private byte rankWinNum;// 등수
    private byte rankClass;// 조 번호
    private int rankNo;// 당첨 번호
}
