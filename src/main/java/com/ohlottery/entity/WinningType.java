package com.ohlottery.entity;

public enum WinningType {
    AUTO, MANUAL;

    public static WinningType fromString(String value) {
        return switch (value) {
            case "자동" -> AUTO;
            case "수동" -> MANUAL;
            default -> null;
        };
    }
}
