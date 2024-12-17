package com.ohlottery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "로또 판매점 정보")
public class LotteryStoreDto {

    @Schema(description = "판매처 ID", example = "1")
    private long storeId;

    @Schema(description = "판매처 이름", example = "1148")
    private String storeName;

    @Schema(description = "판매처 주소", example = "서울특별시 서초구 남부순환로 2423")
    private String storeAddress;
}

