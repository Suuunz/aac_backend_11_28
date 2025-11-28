package com.aac.backend.dto.flutter;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SelectRequestDto {
    private Long sessionId;      // 대화 세션 ID (DB의 PK)
    private String selectedText; // 사용자가 선택한 문장 (예: "아이스 아메리카노 주세요")
}