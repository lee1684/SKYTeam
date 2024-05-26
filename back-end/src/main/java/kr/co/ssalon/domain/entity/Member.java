package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MemberMeeting> joinedMeetings = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Payment> payments = new ArrayList<>();


    private String username;
    private String email;
    private String nickname;
    private String profilePictureUrl;
    private Character gender;
    private String address;
    private String role;
    private String introduction;
    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "member_interests", joinColumns = @JoinColumn(name = "member_id"))
    private List<String> interests = new ArrayList<>();
    private String blackReason;
    private LocalDateTime blackTime;

    @Embedded
    private MemberDates memberDates;

    protected Member() {
    }

    // ***** 필드 메서드 *****
    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname != null ? nickname : this.nickname;
    }

    public void changeRole(String role) {
        this.role = role;
    }

    public void changeProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl != null ? profilePictureUrl : this.profilePictureUrl;
    }

    public void changeGender(Character gender) {
        this.gender = gender != null ? gender : this.gender;
    }

    public void changeAddress(String address) {
        this.address = address != null ? address : this.address;
    }

    public void changeIntroduction(String introduction) {
        this.introduction = introduction != null ? introduction : this.introduction;
    }

    public void changeLastLoginDate() {
        this.memberDates.preUpdate();
    }

    public void initMemberDates() {
        MemberDates memberDates = new MemberDates();
        memberDates.prePersist();
        this.memberDates = memberDates;
    }

    public void addInterests(List<String> interests) {
        this.interests = interests;
    }

    public void changeBlackReason(String blackReason) {
        this.blackReason = blackReason != null ? blackReason : this.blackReason;
    }

    public void changeBlackReasonState(String blackReason) {
        this.blackReason = blackReason;
        this.blackTime = blackReason != null ? LocalDateTime.now() : null;
    }

    public void initDetailSignInfo(String nickname, String profilePictureUrl, Character gender, String address, String introduction, List<String> interests) {
        changeNickname(nickname);
        changeProfilePictureUrl(profilePictureUrl);
        changeGender(gender);
        changeAddress(address);
        changeIntroduction(introduction);
        addInterests(interests);
    }

    // ***** 연관 메서드 *****
    public void addMemberMeeting(MemberMeeting memberMeeting) {
        getJoinedMeetings().add(memberMeeting);
    }

    public void deleteMemberMeeting(MemberMeeting... memberMeeting) {
        getJoinedMeetings().removeAll(Arrays.asList(memberMeeting));
    }

    // ***** 생성 메서드 *****
    public static Member createMember(String username, String email, String role) {
        Member member = Member.builder()
                .username(username)
                .email(email)
                .role(role)
                .build();

        return member;
    }

}
