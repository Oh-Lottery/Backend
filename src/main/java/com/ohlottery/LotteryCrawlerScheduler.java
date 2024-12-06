package com.ohlottery;

import com.ohlottery.service.DHLotteryCrawlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LotteryCrawlerScheduler {

    private final DHLotteryCrawlerService lotteryCrawlerService;

    // 매주 토요일 20:35에 실행 (6/45 로또 크롤링)
    @Scheduled(cron = "0 35 20 * * 6")
    public void scheduleFetchLottery645Data() {
        log.info("Scheduled Task: fetchLottery645Data 실행 중");
        try {
            int latestRound = getLatestLottery645Round();
            lotteryCrawlerService.fetchLottery645Data(latestRound);
        } catch (Exception e) {
            log.error("fetchLottery645Data 실행 중 에러 발생", e);
        }
    }

    // 매주 목요일 19:05에 실행 (연금복권 크롤링)
    @Scheduled(cron = "0 5 19 * * 4")
    public void scheduleFetchLottery720Data() {
        log.info("Scheduled Task: fetchLottery720Data 실행 중");
        try {
            int latestRound = getLatestLottery720Round();
            lotteryCrawlerService.fetchLottery720Data(latestRound);
        } catch (Exception e) {
            log.error("fetchLottery720Data 실행 중 에러 발생", e);
        }
    }

    private int getLatestLottery645Round() {
        // TODO: 가장 최신 회차 정보를 가져오는 로직 추가
        return 1234; // 테스트 값
    }

    private int getLatestLottery720Round() {
        // TODO: 가장 최신 회차 정보를 가져오는 로직 추가
        return 567;
    }
}
