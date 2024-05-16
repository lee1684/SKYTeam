package kr.co.ssalon.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.ssalon.domain.entity.Payment;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {

    @NotNull
    private Long id;
    @NotBlank
    private Long memberId;
    @NotBlank
    private Long meetingId;
    @NotBlank
    private Integer amount;
    @NotBlank
    private String purpose;

    public PaymentDTO(Payment payment) {
        this.id = payment.getId();
        this.memberId = payment.getMember().getId();
        this.meetingId = payment.getMeeting().getId();
        this.amount = payment.getAmount();
        this.purpose = payment.getPurpose();
    }
}
