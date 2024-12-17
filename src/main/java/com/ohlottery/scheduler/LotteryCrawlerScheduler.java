package com.ohlottery.scheduler;

import com.ohlottery.service.DHLotteryCrawlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class LotteryCrawlerScheduler {

    private final DHLotteryCrawlerService lotteryCrawlerService;

    @Scheduled(cron = "0 35 20 * * 6") // 매주 토요일 20:35
    public void scheduleFetchLottery645Data() {
        log.info("Scheduled Task: fetchLottery645Data 실행 중");
        try {
            int latestRound = getLatestLottery645Round();
            log.info("가져온 최신 6/45 로또 회차: {}", latestRound);
            scheduleRetryFetch(()
                    -> lotteryCrawlerService.fetchLottery645Data(latestRound), "6/45", latestRound);
        } catch (Exception e) {
            log.error("fetchLottery645Data 실행 중 에러 발생", e);
        }
    }

    @Scheduled(cron = "0 5 19 * * 4") // 매주 목요일 19:05
    public void scheduleFetchLottery720Data() {
        log.info("Scheduled Task: fetchLottery720Data 실행 중");
        try {
            int latestRound = getLatestLottery720Round();
            log.info("가져온 최신 720 로또 회차: {}", latestRound);
            scheduleRetryFetch(()
                    -> lotteryCrawlerService.fetchLottery720Data(latestRound), "720", latestRound);
        } catch (Exception e) {
            log.error("fetchLottery720Data 실행 중 에러 발생", e);
        }
    }

    private void scheduleRetryFetch(Runnable fetchTask, String lotteryType, int round) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        final int[] retryCount = {0}; // 재시도 횟수
        final int maxRetries = 12; // 최대 12번 재시도 (5분 x 12 = 1시간)

        executor.scheduleAtFixedRate(() -> {
            try {
                log.info("재시도 실행 중: {} 로또 회차 {} | 시도 횟수: {}/{}", lotteryType, round, retryCount[0] + 1, maxRetries);
                fetchTask.run();
                log.info("재시도 성공: {} 로또 회차 {}", lotteryType, round);
                executor.shutdown(); // 성공하면 재시도 종료
            } catch (Exception e) {
                retryCount[0]++;
                log.error("재시도 실패: {} 로또 회차 {} | 시도 횟수: {}/{}", lotteryType, round, retryCount[0], maxRetries, e);
                if (retryCount[0] >= maxRetries) {
                    log.error("재시도 횟수 초과로 중단: {} 로또 회차 {}", lotteryType, round);
                    executor.shutdown(); // 최대 재시도 횟수 초과 시 종료
                }
            }
        }, 0, 5, TimeUnit.MINUTES); // 즉시 실행, 이후 5분 간격
    }

    private int getLatestLottery645Round() {
        try {
            int latestRound = lotteryCrawlerService.getLatest645Round();
            return latestRound + 1;
        } catch (Exception e) {
            log.error("6/45 최신 회차를 가져오는 중 에러 발생", e);
            return -1;
        }
    }

    private int getLatestLottery720Round() {
        try {
            int latestRound = lotteryCrawlerService.getLatest720Round();
            return latestRound + 1;
        } catch (Exception e) {
            log.error("720 최신 회차를 가져오는 중 에러 발생", e);
            return -1;
        }
    }
}
