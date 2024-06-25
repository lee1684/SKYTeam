package kr.co.ssalon.domain.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DiscriminatorValue("A")
@NoArgsConstructor
public class AdvertisePayment extends Payment{

    public AdvertisePayment(Member member, Meeting meeting, Integer amount, String purpose, String tid) {
        super(member, meeting, amount, purpose, tid);
    }

    public static Payment createAdvertisePayment(Member member, Meeting meeting, String purpose, Integer amount, String tid) {
        AdvertisePayment advertisePayment = new AdvertisePayment(member, meeting, amount, purpose, tid);
        advertisePayment.changeMemberByAdvertise(member);
        advertisePayment.changeMeeting(meeting);
        return advertisePayment;
    }

}
