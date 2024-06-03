package kr.co.ssalon.kakaopay.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KakaopayApproveResponseDto {
    private String aid;
    private String cid;
    private String tid;
    private String sid;
    private String partner_order_id;
    private String partner_user_id;
    private String payment_method_type;
    private Amount amount;
    private String item_name;
    private String item_code;
    private LocalDateTime created_at;
    private LocalDateTime approved_at;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Amount {
        private Integer total;
    }
    public Integer returnTotal(){
        return amount.getTotal();
    }
}
