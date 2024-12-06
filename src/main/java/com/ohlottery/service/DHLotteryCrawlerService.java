package com.ohlottery.service;

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
import java.util.Date;
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
            log.warn("해당 회차 {} 데이터가 이미 존재함 - 저장 생략!", round);
            return;
        }

        try {
            String url = "https://dhlottery.co.kr/gameResult.do?method=byWin&drwNo=" + round;
            log.info("URL과 연결 중 : {}", url);

            Document document = Jsoup.connect(url).get();
            log.info("성공적으로 URL과 연결 및 HTML 파싱 완료");

            // 추첨일자 파싱
            Element drawDateElement = document.selectFirst("div.win_result > p.desc");
            if (drawDateElement == null) {
                log.error("drawDateElement를 찾을 수 없습니다. HTML 구조를 확인하세요. 회차: {}", round);
                log.info("HTML 내용: {}", document.html());
                return;
            }
            String drawDateText = drawDateElement.text(); // ex) (2024년 11월 30일 추첨)

            LocalDate drawDate;
            try {
                drawDate = LocalDate.parse(drawDateText.replace("(", "").replace(" 추첨)", ""),
                        DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));
            } catch (DateTimeParseException e) {
                log.error("날짜 파싱 실패! 원본 텍스트: {} | 회차: {}", drawDateText, round, e);
                return;
            }
            log.info("파싱된 drawDate: {}", drawDate);

            Elements numberElements = document.select(".ball_645");
            List<String> numbers = new ArrayList<>();
            for (Element element : numberElements) {
                numbers.add(element.text());
            }

            // 당첨 번호의 갯수 검증
            if (numbers.size() < 6) {
                log.error("당첨 번호가 충분하지 않습니다. HTML 구조를 확인하세요.");
            } else {
                // 메인 번호 (6개)와 보너스 번호 분리
                List<String> mainNumbers = numbers.subList(0, 6);
                String bonusNumber = numbers.get(6);

                log.info("당첨 번호: " + mainNumbers);
                log.info("보너스 번호: " + bonusNumber);
            }

            int drawNo1 = Integer.parseInt(numberElements.get(0).text());
            int drawNo2 = Integer.parseInt(numberElements.get(1).text());
            int drawNo3 = Integer.parseInt(numberElements.get(2).text());
            int drawNo4 = Integer.parseInt(numberElements.get(3).text());
            int drawNo5 = Integer.parseInt(numberElements.get(4).text());
            int drawNo6 = Integer.parseInt(numberElements.get(5).text());
            int bonusNo = Integer.parseInt(numberElements.get(6).text());

            // 당첨금 데이터 추출
            Elements prizeElements = document.select(".tbl_data tbody tr");
            if (prizeElements.isEmpty()) {
                log.error("당첨금 데이터를 찾을 수 없습니다.");
                return;
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

            Lottery645Entity entity = new Lottery645Entity();
            entity.setRound(round);
            entity.setDrawDate(drawDate);
            entity.setDrawNo1((byte) drawNo1);
            entity.setDrawNo2((byte) drawNo2);
            entity.setDrawNo3((byte) drawNo3);
            entity.setDrawNo4((byte) drawNo4);
            entity.setDrawNo5((byte) drawNo5);
            entity.setDrawNo6((byte) drawNo6);
            entity.setBonusNo((byte) bonusNo);
            entity.setFirstAccumulateAmount(firstAccumulateAmount);
            entity.setFirstPrizeWinnerCount(firstPrizeWinnerCount);
            entity.setFirstWinAmount(firstWinAmount);
            entity.setTotalSellAmount(totalSellAmount);

            lottery645Repository.save(entity);
            log.info("해당 회차에 대한 데이터 저장 중 : {}", round);

        } catch (Exception e) {
            log.error("해당 회차에 대한 데이터 저장 중 에러 발생!!!: {}", round, e);
        }
    }

    private long parseLongOrDefault(String input, long defaultValue) {
        try {
            return Long.parseLong(input.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            log.warn("숫자 변환 실패, 기본값 반환: '{}', 입력 값: '{}'", defaultValue, input);
            return defaultValue;
        }
    }

    @Transactional
    public void fetchLottery720Data(long round) {
        log.info("fetchLottery720Data 실행 : {}", round);

        if (lottery720Repository.existsByRound(round)) {
            log.warn("해당 회차 {} 데이터가 이미 존재함 - 저장 생략!", round);
            return;
        }

        try {
            String url = "https://dhlottery.co.kr/gameResult.do?method=win720&drwNo=" + round;
            log.info("URL과 연결 중 : {}", url);

            Document document = Jsoup.connect(url).get();
            log.info("성공적으로 URL과 연결 및 HTML 파싱 완료");

            // 추첨일자 파싱
            Element drawDateElement = document.selectFirst("div.win_result > p.desc");
            if (drawDateElement == null) {
                log.error("drawDateElement를 찾을 수 없습니다. HTML 구조를 확인하세요. 회차: {}", round);
                return;
            }
            String drawDateText = drawDateElement.text(); // ex) (2024년 11월 30일 추첨)

            LocalDate drawDate;
            try {
                drawDate = LocalDate.parse(
                        drawDateText.replace("(", "").replace(" 추첨)", ""),
                        DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
                );
            } catch (DateTimeParseException e) {
                log.error("날짜 파싱 실패! 원본 텍스트: {} | 회차: {}", drawDateText, round, e);
                return;
            }
            log.info("파싱된 drawDate: {}", drawDate);

            Elements prizeElements = document.select(".tbl_data tbody tr");
            if (prizeElements.isEmpty()) {
                log.error("당첨 데이터가 비어 있습니다. HTML 구조를 확인하세요.");
                return;
            }

            // 데이터 저장
            List<Lottery720Entity> entities = new ArrayList<>();
            for (Element row : prizeElements) {
                try {
                    int rankWinNum = Integer.parseInt(row.select("td").get(0).text());
                    int rankClass = Integer.parseInt(row.select("td").get(1).text());
                    int rankNo = Integer.parseInt(row.select("td").get(2).text());

                    Lottery720Entity entity = new Lottery720Entity();
                    entity.setRound(round);
                    entity.setDrawDate(drawDate);
                    entity.setRankWinNum((short) rankWinNum);
                    entity.setRankClass((byte) rankClass);
                    entity.setRankNo(rankNo);

                    entities.add(entity);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    log.warn("데이터 파싱 중 문제가 발생했습니다. 건너뜀. | 회차: {}, 행: {}", round, row.html(), e);
                }
            }

            // 데이터 저장2
            if (!entities.isEmpty()) {
                lottery720Repository.saveAll(entities);
                log.info("회차 {} 데이터 저장 완료. 총 {}개 항목 저장.", round, entities.size());
            } else {
                log.warn("회차 {} 데이터가 저장되지 않았습니다. 유효한 항목이 없습니다.", round);
            }

        } catch (Exception e) {
            log.error("해당 회차에 대한 데이터 저장 중 에러 발생!!!: {}", round, e);
        }
    }

    /* TODO
    동행 복권 사이트 엔드포인트 긁어오기
    6/45는 매주 토요일 20시 35분 -> 5분 후
    720은 매주 목요일 19시 5분 -> 5분 후

    기본 데이터는 엑셀 파일에서 긁어와서 데이터베이스에 마이그레이션
    */

}
