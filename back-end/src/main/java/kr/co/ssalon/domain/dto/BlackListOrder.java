package kr.co.ssalon.domain.dto;

import lombok.Getter;

@Getter
public enum BlackListOrder {

    NUMBER("멤버 번호"),
    RECENT("최신순"),
    NAME("이름순");
    private final String description;

    BlackListOrder(String description) {
        this.description = description;
    }
}
