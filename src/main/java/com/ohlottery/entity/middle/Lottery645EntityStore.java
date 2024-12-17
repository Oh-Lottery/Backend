package com.ohlottery.entity.middle;

import com.ohlottery.entity.Lottery645Entity;
import com.ohlottery.entity.LotteryStoreEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Lottery645EntityStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id")
    private Lottery645Entity lotteryEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private LotteryStoreEntity storeEntity;
}
