package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @OneToMany(mappedBy = "member")
    private final List<MemberMeeting> joinedMeetings = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private final List<Payment> payments = new ArrayList<>();


    private String username;
    private String email;
    private String nickname;
    private String profilePictureUrl;
    private Character gender;
    private String address;
    private String role;
    private String introduction;
    @ElementCollection
    private List<String> interests;
    private String blackReason;

    @Embedded
    private MemberDates memberDates;

    protected Member() {}

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeRole(String role) {
        this.role = role;
    }

    public static Member createMember(String username, String email, String role){
        MemberDates memberDates = new MemberDates();
        memberDates.prePersist();

        Member member = Member.builder()
                .username(username)
                .email(email)
                .role(role)
                .memberDates(memberDates)
                .build();
        return member;
    }

    public void addMemberMeeting(MemberMeeting memberMeeting) {
        this.joinedMeetings.add(memberMeeting);
        memberMeeting.setMember(this);
    }
}
