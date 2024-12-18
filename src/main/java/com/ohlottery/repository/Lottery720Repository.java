package com.ohlottery.repository;

import com.ohlottery.dto.LotteryStoreWinCountDto;
import com.ohlottery.entity.Lottery720Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Lottery720Repository extends JpaRepository<Lottery720Entity, Long> {

    boolean existsByRound(long round);

    @Query("SELECT COALESCE(MAX(l.round), 0) FROM Lottery720Entity l")
    int findMaxRound();

    @Query("select new com.ohlottery.dto.LotteryStoreWinCountDto(store.id, store.storeName, store.storeAddress, count(winning.store.id))" +
            "from LotteryStoreEntity store " +
            "join LotteryWinningEntity winning " +
            "on store.id = winning.store.id and type(winning)=LotteryWinningEntity " +
            "group by store.id, store.storeName, store.storeAddress " +
            "having count(winning.store.id) > :minWinCount " +
            "order by count(winning.store.id) desc")
    List<LotteryStoreWinCountDto> findAllWinCount(int minWinCount);
}
