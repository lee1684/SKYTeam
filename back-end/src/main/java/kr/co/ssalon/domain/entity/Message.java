package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @Column(name = "message_type")
    private String messageType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_meeting_id")
    private MemberMeeting memberMeeting;

    @Column(name = "meeting_id")
    private Long meetingId;

    @Column(name = "member_id")
    private Long memberId;

    private String message;

    private String imageUrl;

    @CreatedDate
    @Column(name = "sent_at", updatable = false)
    private LocalDateTime sentAt;


    protected Message() {}

    public static Message createMessage(MemberMeeting memberMeeting, String message, String messageType, String imageUrl) {
        return Message.builder()
                .messageType(messageType)
                .memberMeeting(memberMeeting)
                .meetingId(memberMeeting.getMeeting().getId())
                .memberId(memberMeeting.getMember().getId())
                .message(message)
                .imageUrl(imageUrl)
                .sentAt(LocalDateTime.now())
                .build();
    }
}
