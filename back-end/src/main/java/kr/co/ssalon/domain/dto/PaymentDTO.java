package kr.co.ssalon.domain.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {

    private Long id;
    private Long memberId;
    private Long meetingId;
    private BigDecimal amount;
    private String purpose;
}
