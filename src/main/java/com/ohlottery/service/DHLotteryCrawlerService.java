package com.ohlottery.service;

import com.ohlottery.repository.Lottery645Repository;
import com.ohlottery.repository.Lottery720Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DHLotteryCrawlerService {

    private final Lottery645Repository lottery645Repository;
    private final Lottery720Repository lottery720Repository;

    public void fetchLottery645Data(int round){

    }

    public void fetchLottery720Data(int round){

    }

    /* TODO
    동행 복권 사이트 엔드포인트 긁어오기
    6/45는 매주 토요일 20시 35분 -> 5분 후
    720은 매주 목요일 19시 5분 -> 5분 후

    기본 데이터는 엑셀 파일에서 긁어와서 데이터베이스에 마이그레이션
    */

}
