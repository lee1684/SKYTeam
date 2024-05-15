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
    private String backgroundColor;
    // 썸네일(증표)?
    // private TicketDTO ticketDTO;
    private String ticketThumb;

    public MeetingHomeSearchDTO(Meeting meeting) {
        this.moimId = meeting.getId();
        this.backgroundColor = meeting.getBackgroundColor();
        // this.ticketDTO = meeting.getTicket() == null ? null : new TicketDTO(meeting.getTicket());
        this.ticketThumb = "https://test-bukkit-240415.s3.ap-northeast-2.amazonaws.com/Thumbnails/" + this.moimId + "/Thumb-" + this.moimId + ".png";
    }
}
