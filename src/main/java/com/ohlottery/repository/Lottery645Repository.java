package com.ohlottery.repository;

import com.ohlottery.entity.Lottery645Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Lottery645Repository extends JpaRepository<Lottery645Entity, Long> {
    
    boolean existsByRound(long round);
    
    @Query("SELECT COALESCE(MAX(l.round), 0) FROM Lottery645Entity l")
    int findMaxRound();

}
