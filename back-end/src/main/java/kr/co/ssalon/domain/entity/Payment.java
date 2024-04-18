package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Entity
@Getter
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

    public static Payment create_payment() {
        Payment payment = new Payment();
        return payment;
    }
}
