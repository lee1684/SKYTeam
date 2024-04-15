package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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
    private String blackReason;

    @Embedded
    private MemberDates memberDates;

    protected Member() {}

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeRole(String role) {
        this.email = role;
    }

    public static Member createMember(String username, String email, String role){
        Member member = Member.builder()
                .username(username)
                .email(email)
                .role(role).build();
        return member;
    }


}
