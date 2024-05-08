package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class MeetingOut {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outreason_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    private String type;
    private String reason;
    private LocalDateTime outTime;

    protected MeetingOut() {}

    // ***** 생성 메서드 *****
    public static MeetingOut createMeetingOutReason(Member member, Meeting meeting, String type, String reason) {
        MeetingOut meetingOut = MeetingOut.builder()
                .member(member)
                .meeting(meeting)
                .type(type)
                .reason(reason)
                .outTime(LocalDateTime.now())
                .build();

        return meetingOut;
    }

}
