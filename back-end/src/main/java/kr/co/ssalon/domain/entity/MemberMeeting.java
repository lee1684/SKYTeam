package kr.co.ssalon.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.coyote.BadRequestException;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "qr_link_id")
    private QrLink qrLink;

    @OneToMany(mappedBy = "memberMeeting", cascade = CascadeType.ALL)
    private final List<Message> messages = new ArrayList<>();

    private boolean attendance;

    protected MemberMeeting() {
    }

    // ***** 연관 메서드 *****
    public void joinMember(Member member) {
        this.member = member;
        member.addMemberMeeting(this);
    }

    public void joinMeeting(Meeting meeting) throws BadRequestException {
        this.meeting = meeting;
        meeting.addParticipants(this);
    }

    public void settingDiary(Diary diary) {
        this.diary = diary;
        diary.settingMemberMeeting(this);
    }

    public void settingQrLink(QrLink qrLink) {
        this.qrLink = qrLink;
        // qrLink.settingMemberMeeting(this);
    }

    public void changeAttendanceTrue() {
        this.attendance = true;
    }

    public void changeAttendanceFalse() {
        this.attendance = false;
    }

    public void changeAttendance(boolean attendance) { this.attendance = attendance; }

    public static MemberMeeting createMemberMeeting(Member member, Meeting meeting) throws BadRequestException {
        List<String> urlList = new ArrayList<>();
        urlList.add("");
        Diary diary = Diary.createDiary("제목을 작성해보세요.", urlList,"일기를 작성해보세요.");
        MemberMeeting memberMeeting = MemberMeeting.builder().build();
        memberMeeting.joinMember(member);
        memberMeeting.joinMeeting(meeting);
        memberMeeting.settingDiary(diary);
        memberMeeting.changeAttendanceFalse();
        return memberMeeting;
    }
}