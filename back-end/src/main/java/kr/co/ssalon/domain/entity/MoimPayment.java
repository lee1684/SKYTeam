package kr.co.ssalon.domain.entity;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DiscriminatorValue("M")
@NoArgsConstructor
public class MoimPayment extends Payment{

    public MoimPayment(Member member, Meeting meeting, Integer amount, String purpose, String tid) {
        super(member, meeting, amount, purpose, tid);
    }
    public static Payment createMoimPayment(Member member, Meeting meeting, String purpose, Integer amount, String tid) {
        MoimPayment moimPayment = new MoimPayment(member, meeting, amount, purpose, tid);
        moimPayment.changeMeeting(meeting);
        moimPayment.changeMemberByMoim(member);
        return moimPayment;
    }
}
