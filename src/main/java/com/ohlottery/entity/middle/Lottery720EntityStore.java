package com.ohlottery.entity.middle;

import com.ohlottery.entity.Lottery720Entity;
import com.ohlottery.entity.LotteryStoreEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Lottery720EntityStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id")
    Lottery720Entity lotteryEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    LotteryStoreEntity storeEntity;
}
