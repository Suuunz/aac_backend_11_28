package com.aac.backend.dto.flutter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppResponseDto {
    private Long sessionId;          // 대화 세션 ID
    private List<String> topKChunks; // AI가 추천한 문장 리스트 (Top-K)
}