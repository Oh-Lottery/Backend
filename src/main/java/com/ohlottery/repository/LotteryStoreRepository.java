package com.ohlottery.repository;

import com.ohlottery.entity.LotteryStoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LotteryStoreRepository extends JpaRepository<LotteryStoreEntity, Long> {
    Optional<LotteryStoreEntity> findByStoreNameAndStoreAddress(String storeName, String storeAddress);
}
