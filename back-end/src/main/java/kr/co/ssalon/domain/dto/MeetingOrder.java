package kr.co.ssalon.domain.dto;

import lombok.Getter;

@Getter
public enum MeetingOrder {
    // 추가 필요
    CAPACITY("인원 수"),
    NUMBER("모임 번호"),
    RECENT("최신순");
    private final String description;

    MeetingOrder(String description) {
        this.description = description;
    }
}
