package kr.co.ssalon.domain.dto;

import lombok.Getter;

@Getter
public enum MeetingFilter {
    // 추가 필요
    CAPACITY("인원 수"),
    NUMBER("모임 번호"),
    RECENT("최신순");
    private final String description;

    MeetingFilter(String description) {
        this.description = description;
    }
}
