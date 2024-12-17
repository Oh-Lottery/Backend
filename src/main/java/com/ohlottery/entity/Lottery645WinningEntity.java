package com.ohlottery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Lottery645WinningEntity extends LotteryWinningEntity {

    @Enumerated(EnumType.STRING)
    private WinningType type;

    public Lottery645WinningEntity(WinningType type) {
        this.type = type;
    }
}
