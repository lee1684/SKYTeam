package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@Getter
@AllArgsConstructor
public abstract class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    private Integer amount;

    private String purpose;
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
    }
}
