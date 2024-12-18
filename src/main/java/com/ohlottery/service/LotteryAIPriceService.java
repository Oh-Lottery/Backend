package com.ohlottery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class LotteryAIPriceService {

    private final URL scriptURL
            = getClass().getClassLoader().getResource("ai/lottery_prediction.py");

    public String executePythonPredictionScript() {
        try {
            // Python 스크립트 실행
            ProcessBuilder processBuilder = new ProcessBuilder("python3", scriptURL.getPath());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Python 출력 결과 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Python 스크립트 실행 실패. Exit code: " + exitCode);
            }

            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Python 스크립트 실행 중 오류 발생: " + e.getMessage());
        }
    }
}
