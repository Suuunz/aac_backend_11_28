package com.aac.backend.controller;

import com.aac.backend.dto.flutter.AppResponseDto;
import com.aac.backend.dto.flutter.InitRequestDto;
import com.aac.backend.dto.flutter.SelectRequestDto;
import com.aac.backend.service.AacService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class AacController {

    private final AacService aacService;

    // 1. 대화 시작 (지도 클릭 -> 녹음 완료 시 호출)
    // POST /api/chat/start
    // Body: { "category": "cafe", "sttText": "아이스 아메리카노" }
    @PostMapping("/start")
    public ResponseEntity<AppResponseDto> startChat(@RequestBody InitRequestDto requestDto) {
        return ResponseEntity.ok(aacService.startConversation(requestDto));
    }

    // 2. 청크 선택 (추천된 문장 중 하나를 클릭했을 때 호출)
    // POST /api/chat/select
    // Body: { "sessionId": 1, "selectedText": "아이스 아메리카노 주세요" }
    @PostMapping("/select")
    public ResponseEntity<AppResponseDto> selectChunk(@RequestBody SelectRequestDto requestDto) {
        return ResponseEntity.ok(aacService.selectChunk(requestDto));
    }
}