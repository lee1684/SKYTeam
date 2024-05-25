package kr.co.ssalon.domain.dto;

import lombok.Getter;

@Getter
public enum MeetingOrder {
    // 추가 필요
    CAPACITY("참가모집 인원 수"),
    NUMBER("모임 번호"),
    RECENT("최신순"),
    DEADLINE("마감일순"),
    MEETING_NAME("모임 이름 사전식"),
    MEETING_NAME_REVERSE("모임 이름 역순 사전식"),
    CHEAP_PARTICIPATION_FEE("참가비 저렴한 순"),
    EXPENSIVE_PARTICIPATION_FEE("참가비 비싼 순");


    
    private final String description;

    MeetingOrder(String description) {
        this.description = description;
    }
}
