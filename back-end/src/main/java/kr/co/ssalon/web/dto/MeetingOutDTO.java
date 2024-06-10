package kr.co.ssalon.web.dto;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.MeetingOut;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingOutDTO {

    private Long id;
    private Long memberId;
    private Long meetingId;
    private String type;
    private String reason;
    private LocalDateTime outTime;

    public MeetingOutDTO(MeetingOut meetingOut) {
        this.id = meetingOut.getId();
        this.memberId = meetingOut.getMember().getId();
        this.meetingId = meetingOut.getMeeting().getId();
        this.type = meetingOut.getType();
        this.reason = meetingOut.getReason();
        this.outTime = meetingOut.getOutTime();
    }
}
