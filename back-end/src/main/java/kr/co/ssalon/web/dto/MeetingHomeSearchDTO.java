package kr.co.ssalon.web.dto;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.service.MeetingService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.coyote.BadRequestException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetingHomeSearchDTO {
    // 모임Id
    private Long moimId;
    private String meetingTitle;
    private Boolean isCreator;
    private String backgroundColor;
    // 썸네일(증표)?
    // private TicketDTO ticketDTO;
    private String ticketThumb;

    public MeetingHomeSearchDTO(Meeting meeting, String username){
        this.moimId = meeting.getId();
        this.meetingTitle = meeting.getTitle();
        this.isCreator = meeting.getCreator().getUsername().equals(username);
        this.backgroundColor = meeting.getBackgroundColor();
        // this.ticketDTO = meeting.getTicket() == null ? null : new TicketDTO(meeting.getTicket());
        this.ticketThumb = meeting.getThumbnail();
    }
}
