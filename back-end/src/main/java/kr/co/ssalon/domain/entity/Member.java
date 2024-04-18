package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @OneToMany(mappedBy = "member")
    private List<MemberMeeting> joinedMeetings = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Payment> payments = new ArrayList<>();


    private String name;
    private String email;
    private String nickname;
    private String profilePictureUrl;
    private Character gender;
    private String address;
    private String role;
    private String blackReason;
    @Embedded
    private MemberDates memberDates;

    protected Member() {}

    public static Member create_member(){
        Member member = new Member();
        return member;
    }


}
