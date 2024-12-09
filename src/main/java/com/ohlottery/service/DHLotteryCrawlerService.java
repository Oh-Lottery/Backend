package com.ohlottery.service;

import com.ohlottery.dto.Lottery645Dto;
import com.ohlottery.dto.Lottery720Dto;
import com.ohlottery.entity.Lottery645Entity;
import com.ohlottery.entity.Lottery720Entity;
import com.ohlottery.repository.Lottery645Repository;
import com.ohlottery.repository.Lottery720Repository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Builder
@RequiredArgsConstructor
public class DHLotteryCrawlerService {

    private final Lottery645Repository lottery645Repository;
    private final Lottery720Repository lottery720Repository;

    @Transactional
    public void fetchLottery645Data(long round) {
        log.info("fetchLottery645Data 실행 : {}", round);

        if (lottery645Repository.existsByRound(round)) {
            log.warn("{}회차 데이터가 이미 존재함 - 저장 생략!", round);
            return;
        }

        try {
            Lottery645Dto dto = crawlLottery645Data(round);
            Lottery645Entity entity = convertToEntity(dto);
            lottery645Repository.save(entity);
            log.info("{}회차 데이터 저장 완료!", round);

        } catch (Exception e) {
            log.error("{}회차 데이터 저장 중 에러 발생: {}", round, e.getMessage(), e);
        }
    }

    private Lottery645Dto crawlLottery645Data(long round) throws Exception {
        log.info("crawlLottery645Data 실행 : {}", round);
        String url = "https://dhlottery.co.kr/gameResult.do?method=byWin&drwNo=" + round;
        log.info("URL과 연결 중 : {}", url);

        Document document = Jsoup.connect(url).get();
        log.info("성공적으로 URL과 연결 및 HTML 파싱 완료");

        // 추첨일자 파싱
        Element drawDateElement = document.selectFirst("div.win_result > p.desc");
        if (drawDateElement == null) {
            log.error("drawDateElement를 찾을 수 없습니다. HTML 구조를 확인하세요. 회차: {}", round);
            throw new IllegalArgumentException("부적합한 HTML 구조");
        }
        String drawDateText = drawDateElement.text(); // ex) (2024년 11월 30일 추첨)

        LocalDate drawDate = LocalDate.parse(
                drawDateText.replace("(", "").replace(" 추첨)", ""),
                DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
        );
        log.info("파싱된 drawDate: {}", drawDate);

        Elements numberElements = document.select(".ball_645");
        if (numberElements.size() < 7) {
            log.error("당첨 번호가 충분하지 않습니다. HTML 구조를 확인하세요.");
            throw new IllegalArgumentException("당첨번호 개수 부족");
        }

        int drawNo1 = Integer.parseInt(numberElements.get(0).text());
        int drawNo2 = Integer.parseInt(numberElements.get(1).text());
        int drawNo3 = Integer.parseInt(numberElements.get(2).text());
        int drawNo4 = Integer.parseInt(numberElements.get(3).text());
        int drawNo5 = Integer.parseInt(numberElements.get(4).text());
        int drawNo6 = Integer.parseInt(numberElements.get(5).text());
        int bonusNo = Integer.parseInt(numberElements.get(6).text());

        log.info("당첨 번호: [{}, {}, {}, {}, {}, {}], 보너스 번호: {}", drawNo1, drawNo2, drawNo3, drawNo4, drawNo5, drawNo6, bonusNo);

        // 당첨금 데이터 추출
        Elements prizeElements = document.select(".tbl_data tbody tr");
        if (prizeElements.isEmpty()) {
            log.error("당첨금 데이터를 찾을 수 없습니다.");
            throw new IllegalArgumentException("당첨금 데이터 실종");
        }

        long firstAccumulateAmount = parseLongOrDefault(
                prizeElements.get(0).select("td").get(1).text(), 0L);
        long firstPrizeWinnerCount = parseLongOrDefault(
                prizeElements.get(0).select("td").get(2).text(), 0L);
        long firstWinAmount = parseLongOrDefault(
                prizeElements.get(0).select("td").get(3).text(), 0L);
        long totalSellAmount = parseLongOrDefault(
                document.select(".total strong").text(), 0L);
        log.info("1등 관련 자료 파싱 완료");

        return Lottery645Dto.builder()
                .round(round)
                .drawDate(drawDate)
                .drawNo1(drawNo1)
                .drawNo2(drawNo2)
                .drawNo3(drawNo3)
                .drawNo4(drawNo4)
                .drawNo5(drawNo5)
                .drawNo6(drawNo6)
                .bonusNo(bonusNo)
                .firstAccumulateAmount(firstAccumulateAmount)
                .firstPrizeWinnerCount(firstPrizeWinnerCount)
                .firstWinAmount(firstWinAmount)
                .totalSellAmount(totalSellAmount)
                .build();
    }

    private Lottery645Entity convertToEntity(Lottery645Dto dto) {
        return Lottery645Entity.builder()
                .round(dto.getRound())
                .drawDate(dto.getDrawDate())
                .drawNo1((byte) dto.getDrawNo1())
                .drawNo2((byte) dto.getDrawNo2())
                .drawNo3((byte) dto.getDrawNo3())
                .drawNo4((byte) dto.getDrawNo4())
                .drawNo5((byte) dto.getDrawNo5())
                .drawNo6((byte) dto.getDrawNo6())
                .bonusNo((byte) dto.getBonusNo())
                .firstAccumulateAmount(dto.getFirstAccumulateAmount())
                .firstPrizeWinnerCount(dto.getFirstPrizeWinnerCount())
                .firstWinAmount(dto.getFirstWinAmount())
                .totalSellAmount(dto.getTotalSellAmount())
                .build();
    }

    private long parseLongOrDefault(String input, long defaultValue) {
        try {
            return Long.parseLong(input.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            log.warn("숫자 변환 실패, 기본값 반환: '{}', 입력 값: '{}'", defaultValue, input);
            return defaultValue;
        }
    }

    public int getLatest645Round() {
        return lottery645Repository.findMaxRound();
    }

    @Transactional
    public void fetchLottery720Data(long round) {
        log.info("fetchLottery720Data 실행 : {}", round);

        if (lottery720Repository.existsByRound(round)) {
            log.warn("해당 회차 {} 데이터가 이미 존재함 - 저장 생략!", round);
            return;
        }

        try {
            Lottery720Dto dto = crawlLottery720Data(round); // DTO 생성
            Lottery720Entity entity = convertToEntityUsingBuilder(dto); // DTO를 Entity로 변환
            lottery720Repository.save(entity); // 엔티티 저장
            log.info("회차 {} 데이터 저장 완료.", round);

        } catch (Exception e) {
            log.error("해당 회차에 대한 데이터 저장 중 에러 발생: {}", round, e);
        }
    }


    private Lottery720Dto crawlLottery720Data(long round) throws Exception {
        log.info("crawlLottery720Data 실행 : {}", round);
        String url = "https://dhlottery.co.kr/gameResult.do?method=win720&drwNo=" + round;
        log.info("URL과 연결 중 : {}", url);

        Document document = Jsoup.connect(url).get();
        log.info("성공적으로 URL과 연결 및 HTML 파싱 완료");

        // 추첨일자 파싱
        Element drawDateElement = document.selectFirst("div.win_result > p.desc");
        if (drawDateElement == null) {
            log.error("drawDateElement를 찾을 수 없습니다. HTML 구조를 확인하세요. 회차: {}", round);
            throw new IllegalArgumentException("부적합한 HTML 구조");
        }

        String drawDateText = drawDateElement.text(); // ex) (2024년 12월 05일 추첨)
        LocalDate drawDate = LocalDate.parse(
                drawDateText.replace("(", "").replace(" 추첨)", ""),
                DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
        );

        log.info("파싱된 drawDate: {}", drawDate);

        // 당첨 데이터 파싱
        Elements prizeElements = document.select(".tbl_data tbody tr");
        if (prizeElements.isEmpty()) {
            log.error("당첨 데이터가 비어 있습니다. HTML 구조를 확인하세요.");
            throw new IllegalArgumentException("당첨 데이터 부족");
        }

        Element firstRow = prizeElements.get(0);
        int rankWinNum = parseIntOrDefault(firstRow.select("td").get(0).text(), -1);
        int rankClass = parseIntOrDefault(firstRow.select("td").get(1).text(), -1);
        int rankNo = parseIntOrDefault(firstRow.select("td").get(2).text(), -1);

        if (rankWinNum == -1 || rankClass == -1 || rankNo == -1) {
            throw new IllegalArgumentException("당첨 데이터에 숫자로 변환할 수 없는 값이 포함되어 있습니다.");
        }

        return Lottery720Dto.builder()
                .round(round)
                .drawDate(drawDate)
                .rankWinNum((short) rankWinNum)
                .rankClass((byte) rankClass)
                .rankNo(rankNo)
                .build();
    }

    private Lottery720Entity convertToEntityUsingBuilder(Lottery720Dto dto) {
        return Lottery720Entity.builder()
                .round(dto.getRound())
                .drawDate(dto.getDrawDate())
                .rankWinNum(dto.getRankWinNum())
                .rankClass((byte) dto.getRankClass())
                .rankNo(dto.getRankNo())
                .build();
    }

    private int parseIntOrDefault(String input, int defaultValue) {
        try {
            String numericValue = input.replaceAll("[^0-9]", "");
            return Integer.parseInt(numericValue);
        } catch (NumberFormatException e) {
            log.warn("문자열을 숫자로 변환하지 못했습니다. 기본값 반환: '{}', 원본 값: '{}'", defaultValue, input, e);
            return defaultValue;
        }
    }

    public int getLatest720Round() {
        return lottery720Repository.findMaxRound();
    }

    /* TODO
    동행 복권 사이트 엔드포인트 긁어오기
    6/45는 매주 토요일 20시 35분 -> 5분 후
    720은 매주 목요일 19시 5분 -> 5분 후

    기본 데이터는 엑셀 파일에서 긁어와서 데이터베이스에 마이그레이션
    */

}
