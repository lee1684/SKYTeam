package kr.co.ssalon.domain.dto;

import lombok.Getter;

@Getter
public enum ReportOrder {

    NUMBER("신고 번호"),
    RECENT("최신순");
    private final String description;

    ReportOrder(String description) {
        this.description = description;
    }
}
