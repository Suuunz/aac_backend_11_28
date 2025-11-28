package com.aac.backend.dto.fastapi;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AiResponseDto {
    // FastAPI가 {"recommendations": ["문장1", "문장2"]} 형태로 준다고 가정
    private List<String> recommendations;
}