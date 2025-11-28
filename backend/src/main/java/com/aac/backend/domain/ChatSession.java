package com.aac.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 세션 ID (Flutter가 들고 있어야 함)

    private String category; // 매장 카테고리
    private String sttText;  // 최초 STT 텍스트

    @ElementCollection
    private List<String> selectedHistory = new ArrayList<>(); // 이미 선택된 청크들

    @Builder
    public ChatSession(String category, String sttText) {
        this.category = category;
        this.sttText = sttText;
    }

    // 청크 선택 시 기록 추가
    public void addSelection(String selectedChunk) {
        this.selectedHistory.add(selectedChunk);
    }
}