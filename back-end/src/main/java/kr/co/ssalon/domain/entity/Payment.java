package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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

    private Integer amount;

    private String purpose;

    protected Payment() {}

    public void changeMember(Member member) {
        this.member = member;
    }

    public static Payment createPayment(Member member, Integer amount) {
        Payment payment = Payment.builder()
                .amount(amount)
                .build();

        payment.changeMember(member);

        return payment;
    }
}
