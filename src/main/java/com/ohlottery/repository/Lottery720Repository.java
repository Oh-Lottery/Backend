package com.ohlottery.repository;

import com.ohlottery.entity.Lottery720Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Lottery720Repository extends JpaRepository<Lottery720Entity, Long> {
    boolean existsByRound(long round);
    @Query("SELECT COALESCE(MAX(l.round), 0) FROM Lottery720Entity l")
    int findMaxRound();

}
