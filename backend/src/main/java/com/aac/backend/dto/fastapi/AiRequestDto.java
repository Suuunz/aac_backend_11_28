package com.aac.backend.dto.fastapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AiRequestDto {

    private String category; // 매장 카테고리

    @JsonProperty("stt_text") // JSON으로 변환될 때 "stt_text" 라는 이름으로 변환됨
    private String sttText;

    @JsonProperty("history")  // 이미 선택된 청크 리스트 (대화 맥락)
    private List<String> history;
}