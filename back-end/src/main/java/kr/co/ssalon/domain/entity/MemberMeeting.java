package kr.co.ssalon.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class MemberMeeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_meeting_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @OneToMany(mappedBy = "memberMeeting")
    private List<Message> messages = new ArrayList<>();

    protected MemberMeeting() {}

    public static MemberMeeting create_memberMeeting() {
        MemberMeeting memberMeeting = new MemberMeeting();
        return memberMeeting;
    }
}
