package kr.co.ssalon.kakaopay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KakaopayCancelRequestDto {
    private String cid;
    private String tid;
    private Integer cancelAmount;
    private Integer cancelTaxFreeAmount;


}
