package com.ohlottery.entity;

import com.ohlottery.entity.middle.Lottery720EntityStore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Lottery720Entity {

    @Id
    private long round;// 회차

    private LocalDate drawDate;// 추첨 날짜

    private byte rankClass;// 조 번호
    private int rankNo;// 당첨 번호
    private int bonusNo;// 보너스 번호

    @OneToMany(mappedBy = "lotteryEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Lottery720EntityStore> storeList = new ArrayList<>();
}
