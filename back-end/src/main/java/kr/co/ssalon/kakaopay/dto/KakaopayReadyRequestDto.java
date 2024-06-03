package kr.co.ssalon.kakaopay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KakaopayReadyRequestDto {
    private String cid;
    private String partnerOrderId;
    private String partnerUserId;
    private String itemName;
    private String itemCode;
    private Integer quantity;
    private Integer totalPayment;
    private Integer taxFreeAmount;
    private String approvalUrl;
    private String cancelUrl;
    private String failUrl;
}
