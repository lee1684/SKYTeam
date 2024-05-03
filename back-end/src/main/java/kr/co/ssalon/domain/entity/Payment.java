package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    private Meeting meeting;

    private BigDecimal amount;

    private String purpose;

    protected Payment() {}

    public static Payment createPayment() {
        Payment payment = Payment.builder().build();
        return payment;
    }

    public static Payment createPayment(Member member, Meeting meeting) {
        Payment payment = Payment.builder()
                .member(member)
                .meeting(meeting)
                .amount(BigDecimal.ZERO)
                .purpose(null)
                .build();

        return payment;
    }
}
