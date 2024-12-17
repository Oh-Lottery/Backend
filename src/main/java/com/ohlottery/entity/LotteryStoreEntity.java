package com.ohlottery.entity;

import com.ohlottery.entity.middle.Lottery645EntityStore;
import com.ohlottery.entity.middle.Lottery720EntityStore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class LotteryStoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storeName;
    private String storeAddress;

    @OneToMany(mappedBy = "storeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Lottery645EntityStore> entity645List = new ArrayList<>();

    @OneToMany(mappedBy = "storeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Lottery720EntityStore> entity720List = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<LotteryWinningEntity> winningList = new ArrayList<>();

    public LotteryStoreEntity(String storeName, String storeAddress) {
        this.storeName = storeName;
        this.storeAddress = storeAddress;
    }
}

