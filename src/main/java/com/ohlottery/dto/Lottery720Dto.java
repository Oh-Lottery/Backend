package com.ohlottery.dto;

import com.ohlottery.entity.Lottery720Entity;
import lombok.Data;

import java.util.Date;

@Data
public class Lottery720Dto {

    public Lottery720Dto(Lottery720Entity entity) {
        round = entity.getRound();
        drawDate = entity.getDrawDate();
        rankWinNum = entity.getRankWinNum();
        rankClass = entity.getRankClass();
        rankNo = entity.getRankNo();
    }

    private long round;// 회차

    private Date drawDate;// 추첨 날짜

    private short rankWinNum;// 등수
    private byte rankClass;// 조 번호
    private int rankNo;// 당첨 번호
}
