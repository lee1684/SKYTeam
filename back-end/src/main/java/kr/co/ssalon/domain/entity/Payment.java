package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

<<<<<<< HEAD
import java.math.BigDecimal;
import java.text.DecimalFormat;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@Getter
@AllArgsConstructor
public abstract class Payment {
=======
@Entity
@Getter
@Builder
@AllArgsConstructor
public class Payment {
>>>>>>> develop

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

<<<<<<< HEAD
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
=======
    @OneToOne(fetch = FetchType.LAZY)
>>>>>>> develop
    private Meeting meeting;

    private Integer amount;

    private String purpose;
<<<<<<< HEAD
    private String tid;
    @Column(insertable = false, updatable = false)
    private String dtype;

    public Payment(Member member, Meeting meeting, Integer amount, String purpose, String tid) {
        this.member = member;
        this.meeting = meeting;
        this.amount = amount;
        this.purpose = purpose;
        this.tid = tid;
    }

    protected Payment() {
    }

    // ***** 연관 메서드 *****
    public void changeMemberByMoim(Member member) {
        this.member = member;
        member.getPayments().add(this);
    }

    public void changeMemberByAdvertise(Member member) {
        this.member = member;
        member.getAdvertisements().add(this);
    }

    public void changeMeeting(Meeting meeting) {
        this.meeting = meeting;
=======

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
>>>>>>> develop
    }
}
