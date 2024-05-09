package kr.co.ssalon.web.dto;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.MemberMeeting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingInfoDTO {

    private Long id;
    private String category;
    private Long paymentId;
    private String creator;
    private Long ticketId;
    private List<Long> participantIds;
    private List<String> meetingPictureUrls;
    private String title;
    private String description;
    private String location;
    private Integer capacity;
    private LocalDateTime meetingDate;

    public MeetingInfoDTO(Meeting meeting) {
        this.id = meeting.getId();
        this.category = meeting.getCategory().getName();
        this.paymentId = meeting.getPayment() == null ? null : meeting.getPayment().getId();
        this.creator = meeting.getCreator().getNickname();
        this.ticketId = meeting.getTicket() == null ? null : meeting.getTicket().getId();
        this.participantIds = meeting.getParticipants().stream().map(MemberMeeting::getId).collect(Collectors.toList());
        this.meetingPictureUrls = meeting.getMeetingPictureUrls();
        this.title = meeting.getTitle();
        this.description = meeting.getDescription();
        this.location = meeting.getLocation();
        this.capacity = meeting.getCapacity();
        this.meetingDate = meeting.getMeetingDate();
    }
}
