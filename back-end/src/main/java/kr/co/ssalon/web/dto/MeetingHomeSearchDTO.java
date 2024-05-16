package kr.co.ssalon.web.dto;

import kr.co.ssalon.domain.entity.Meeting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetingHomeSearchDTO {
    // 모임Id
    private Long moimId;
    private String meetingTitle;
    private String backgroundColor;
    // 썸네일(증표)?
    // private TicketDTO ticketDTO;
    private String ticketThumb;

    public MeetingHomeSearchDTO(Meeting meeting) {
        this.moimId = meeting.getId();
        this.meetingTitle = meeting.getTitle();
        this.backgroundColor = meeting.getBackgroundColor();
        // this.ticketDTO = meeting.getTicket() == null ? null : new TicketDTO(meeting.getTicket());
        this.ticketThumb = meeting.getThumbnail();
    }
}
