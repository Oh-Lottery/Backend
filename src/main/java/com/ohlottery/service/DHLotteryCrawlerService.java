package com.ohlottery.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ohlottery.entity.Lottery645Entity;
import com.ohlottery.entity.Lottery720Entity;
import com.ohlottery.repository.Lottery645Repository;
import com.ohlottery.repository.Lottery720Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class DHLotteryCrawlerService {

    private final Lottery645Repository lottery645Repository;
    private final Lottery720Repository lottery720Repository;

    @Async
    @Transactional
    public void fetchAllLotteryData() {
        //모든 데이터가 fail이랑 없다고 뜰 때 까지 모든 데이터 긁어오기
        log.info("Started fetching Lottery 645 data.");
        try {
            for(int round=1; ; round++) {
                if (lottery645Repository.existsByRound(round)) {
                    log.warn("해당 회차 {} 데이터가 이미 존재함 - 저장 생략!", round);
                    continue;
                }

                Lottery645Entity entity = crawlLottery645Data(round);
                lottery645Repository.save(entity);
                log.info("{}회차 데이터 저장 완료!", round);
            }
        } catch (IllegalArgumentException ignored) {
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Completed fetching Lottery 645 data.");

        log.info("Started fetching Lottery 645 data.");
        try {
            for(int round=1; ; round++) {
                if (lottery720Repository.existsByRound(round)) {
                    log.warn("해당 회차 {} 데이터가 이미 존재함 - 저장 생략!", round);
                    continue;
                }

                Lottery720Entity entity = crawlLottery720Data(round);
                lottery720Repository.save(entity);
                log.info("{}회차 데이터 저장 완료!", round);
            }
        } catch (IllegalArgumentException ignored) {
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Completed fetching Lottery 720 data.");
    }

    @Async
    @Transactional
    public void fetchLottery645Data(long round) {
        log.info("fetchLottery645Data 실행 : {}", round);

        if (lottery645Repository.existsByRound(round)) {
            log.warn("{}회차 데이터가 이미 존재함 - 저장 생략!", round);
            return;
        }

        try {
            Lottery645Entity entity = crawlLottery645Data(round);
            lottery645Repository.save(entity);
            log.info("{}회차 데이터 저장 완료!", round);
        } catch (Exception e) {
            log.error("{}회차 데이터 저장 중 에러 발생: {}", round, e.getMessage(), e);
        }
    }

    @Async
    @Transactional
    public void fetchLottery720Data(long round) {
        log.info("fetchLottery720Data 실행 : {}", round);

        if (lottery720Repository.existsByRound(round)) {
            log.warn("해당 회차 {} 데이터가 이미 존재함 - 저장 생략!", round);
            return;
        }

        try {
            Lottery720Entity entity = crawlLottery720Data(round);
            lottery720Repository.save(entity); // 엔티티 저장
            log.info("회차 {} 데이터 저장 완료.", round);
        } catch (Exception e) {
            log.error("해당 회차에 대한 데이터 저장 중 에러 발생: {}", round, e);
        }
    }

    private Lottery645Entity crawlLottery645Data(long round) throws Exception {
        //Text만 추출
        String url = "https://dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=" + round;

        log.info("URL과 연결 중 : {}", url);
        String jsonString = Jsoup.connect(url)
                .get()
                .body()
                .text();
        log.info("{}회차 API fetch 성공", round);

        //Json 변환
        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
        if (json.get("returnValue").getAsString().equals("fail"))
            throw new IllegalArgumentException("아직 추첨되지 않은 회차");

        Lottery645Entity entity = new Lottery645Entity(
                json.get("drwNo").getAsLong(),
                LocalDate.parse(json.get("drwNoDate").getAsString()),
                json.get("drwtNo1").getAsByte(),
                json.get("drwtNo2").getAsByte(),
                json.get("drwtNo3").getAsByte(),
                json.get("drwtNo4").getAsByte(),
                json.get("drwtNo5").getAsByte(),
                json.get("drwtNo6").getAsByte(),
                json.get("bnusNo").getAsByte(),
                json.get("firstAccumamnt").getAsLong(),
                json.get("firstPrzwnerCo").getAsLong(),
                json.get("firstWinamnt").getAsLong(),
                json.get("totSellamnt").getAsLong()
        );

        log.info("[{}] 당첨 번호: [{}, {}, {}, {}, {}, {}], 보너스 번호: {}",
                entity.getRound(),
                entity.getDrawNo1(), entity.getDrawNo2(), entity.getDrawNo3(),
                entity.getDrawNo4(), entity.getDrawNo5(), entity.getDrawNo6(),
                entity.getBonusNo());

        return entity;
    }

    private Lottery720Entity crawlLottery720Data(long round) throws Exception {
        //Text만 추출
        String url = "https://dhlottery.co.kr/common.do?method=get720Number&drwNo=" + round;

        log.info("URL과 연결 중 : {}", url);
        String jsonString = Jsoup.connect(url)
                .get()
                .body()
                .text();
        log.info("{}회차 API fetch 성공", round);

        //Json 변환
        JsonObject json = JsonParser.parseString(jsonString).getAsJsonObject();
        if (json.get("rows1").getAsJsonArray().isEmpty())
            throw new IllegalArgumentException("아직 추첨되지 않은 회차");

        JsonArray winnerData = json.get("rows2") //당첨 정보
                .getAsJsonArray();

        JsonObject first = winnerData.get(0).getAsJsonObject();
        JsonObject bonus = winnerData.get(7).getAsJsonObject();

        Lottery720Entity entity = new Lottery720Entity(
                first.get("round").getAsLong(),
                LocalDate.parse(first.get("pensionDrawDate").getAsString()),
                first.get("rankClass").getAsByte(),
                first.get("rankNo").getAsInt(),
                bonus.get("rankNo").getAsInt()
        );

        log.info("[{}] 당첨 번호: [{}조 {}], 보너스 번호: {}",
                entity.getRound(),
                entity.getRankClass(), entity.getRankNo(),
                entity.getBonusNo());

        return entity;
    }

    public int getLatest645Round() {
        return lottery645Repository.findMaxRound();
    }

    public int getLatest720Round() {
        return lottery720Repository.findMaxRound();
    }

}
