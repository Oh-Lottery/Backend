package com.ohlottery.service;

import com.ohlottery.entity.Lottery645Entity;
import com.ohlottery.entity.Lottery720Entity;
import com.ohlottery.repository.Lottery645Repository;
import com.ohlottery.repository.Lottery720Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LotteryService {

    private final Lottery645Repository lottery645Repository;
    private final Lottery720Repository lottery720Repository;

    public Optional<Lottery645Entity> getLottery645Result(long round) {
        return lottery645Repository.findById(round);
    }

    public Optional<Lottery720Entity> getLottery720Result(long round){
        return lottery720Repository.findById(round);
    }

}
