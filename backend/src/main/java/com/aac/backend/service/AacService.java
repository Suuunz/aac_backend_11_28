package com.aac.backend.service;

import com.aac.backend.domain.ChatSession;
import com.aac.backend.dto.fastapi.AiRequestDto;
import com.aac.backend.dto.fastapi.AiResponseDto;
import com.aac.backend.dto.flutter.AppResponseDto;
import com.aac.backend.dto.flutter.InitRequestDto;
import com.aac.backend.dto.flutter.SelectRequestDto;
import com.aac.backend.repository.ChatSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AacService {

    private final ChatSessionRepository chatSessionRepository;
    private final RestTemplate restTemplate;

    // TODO: 로컬 테스트 시 localhost, 실제 배포 시 FastAPI 서버 IP 입력
    private static final String FASTAPI_URL = "http://localhost:8000/recommend";

    /**
     * 1. 초기 요청 처리 (세션 생성 -> FastAPI 호출)
     */
    @Transactional
    public AppResponseDto startConversation(InitRequestDto requestDto) {
        // DB에 세션 저장
        ChatSession session = chatSessionRepository.save(
                ChatSession.builder()
                        .category(requestDto.getCategory())
                        .sttText(requestDto.getSttText())
                        .build()
        );

        // FastAPI 호출
        List<String> recommendations = callAiServer(session);

        return new AppResponseDto(session.getId(), recommendations);
    }

    /**
     * 2. 선택 요청 처리 (기록 업데이트 -> FastAPI 재호출)
     */
    @Transactional
    public AppResponseDto selectChunk(SelectRequestDto requestDto) {
        // 세션 조회
        ChatSession session = chatSessionRepository.findById(requestDto.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 세션입니다. ID=" + requestDto.getSessionId()));

        // 선택한 청크를 DB 히스토리에 저장
        session.addSelection(requestDto.getSelectedText());
        // JPA Dirty Checking으로 자동 update 쿼리 실행됨

        // FastAPI 호출 (업데이트된 히스토리 포함)
        List<String> recommendations = callAiServer(session);

        return new AppResponseDto(session.getId(), recommendations);
    }

    // [공통] FastAPI 서버 통신 로직
    private List<String> callAiServer(ChatSession session) {
        try {
            // 요청 데이터 생성
            AiRequestDto aiRequest = AiRequestDto.builder()
                    .category(session.getCategory())
                    .sttText(session.getSttText())
                    .history(session.getSelectedHistory()) // 누적된 선택지 전송
                    .build();

            log.info("FastAPI 요청: {}", aiRequest);

            // POST 요청
            ResponseEntity<AiResponseDto> response = restTemplate.postForEntity(
                    FASTAPI_URL,
                    aiRequest,
                    AiResponseDto.class
            );

            if (response.getBody() != null && response.getBody().getRecommendations() != null) {
                log.info("FastAPI 응답: {}", response.getBody().getRecommendations());
                return response.getBody().getRecommendations();
            }
        } catch (Exception e) {
            log.error("FastAPI 연결 실패: {}", e.getMessage());
        }

        // 실패 시 더미 데이터 반환
        return List.of("서버 연결 실패", "다시 시도해주세요");
    }
}