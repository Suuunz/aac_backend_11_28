package com.aac.backend.dto.flutter;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InitRequestDto {
    private String category; // 매장 카테고리 (예: "cafe")
    private String sttText;  // 음성 인식 텍스트 (예: "아메리카노")
}