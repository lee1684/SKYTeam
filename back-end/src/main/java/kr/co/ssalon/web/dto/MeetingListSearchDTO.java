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
public class MeetingListSearchDTO {

    // 모임Id
    private Long moimId;
    // 필터를 위한 타입 카테고리 이름?
    private String categoryName;

    // 썸네일(증표)?
    private TicketDTO ticketDTO;

    public MeetingListSearchDTO(Meeting meeting) {
        this.moimId = meeting.getId();
        this.categoryName = meeting.getCategory().getName();
        this.ticketDTO = meeting.getTicket() == null ? null : new TicketDTO(meeting.getTicket());
    }

}
