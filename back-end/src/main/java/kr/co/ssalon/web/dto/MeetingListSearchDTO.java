package kr.co.ssalon.web.dto;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetingListSearchDTO {

    // 모임Id
    private Long moimId;
    // 필터를 위한 타입 카테고리 이름?
    private String categoryName;

    private String meetingTitle;

    private Boolean isCreator;

    private String backgroundColor;
    // 썸네일(증표)?
    // private TicketDTO ticketDTO;
    private String ticketThumb;

    public MeetingListSearchDTO(Meeting meeting, String username) {
        this.moimId = meeting.getId();
        this.categoryName = meeting.getCategory().getName();
        this.meetingTitle = meeting.getTitle();
        this.isCreator = meeting.getCreator().getUsername().equals(username);
        this.backgroundColor = meeting.getBackgroundColor();
        // this.ticketDTO = meeting.getTicket() == null ? null : new TicketDTO(meeting.getTicket());
        this.ticketThumb = "https://test-bukkit-240415.s3.ap-northeast-2.amazonaws.com/Thumbnails/" + this.moimId + "/Thumb-" + this.moimId + ".png";
    }

}
