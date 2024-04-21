package kr.co.ssalon.domain.entity;

import lombok.Getter;

@Getter
public enum Region {

    SEOUL("서울특별시"),
    GYEONGGIDO("경기도"),
    GANGWONDO("강원도"),
    GYEONGSANGNAMDO("경상남도"),
    GYEONGSANGBUKDO("경상북도");

    private final String localName;

    Region(String localName) {
        this.localName = localName;
    }
}
