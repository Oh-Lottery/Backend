package com.ohlottery.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class LotteryAINumberService {

    public JsonNode getAINumberResults() {
        JsonNode aiNumberResults = null;

        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python3", "/app/ai/lottery_probability_predictor.py");
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            aiNumberResults = objectMapper.readTree(output.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return aiNumberResults;
    }
}
