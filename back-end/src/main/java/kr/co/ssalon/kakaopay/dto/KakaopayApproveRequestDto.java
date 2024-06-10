package kr.co.ssalon.kakaopay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KakaopayApproveRequestDto {
    private String tid;
    private String cid;
    private String partnerOrderId;
    private String partnerUserId;
    private String pgToken;
}
