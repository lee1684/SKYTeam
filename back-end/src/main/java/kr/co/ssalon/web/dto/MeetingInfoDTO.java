package kr.co.ssalon.web.dto;
import kr.co.ssalon.domain.entity.Meeting;
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
    private Integer payment;
    private String creator;
    private List<MemberSignDTO> participants;
    private List<String> meetingPictureUrls;
    private String title;
    private String description;
    private String location;
    private Integer capacity;
    private LocalDateTime meetingDate;
    private Boolean isSharable;
    private String backgroundColor;

    public MeetingInfoDTO(Meeting meeting) {
        this.id = meeting.getId();
        this.category = meeting.getCategory().getName();
        this.payment = meeting.getPayment();
        this.backgroundColor = meeting.getBackgroundColor();
        this.creator = meeting.getCreator().getNickname();
        this.participants = meeting.getParticipants().stream().map(memberMeeting -> new MemberSignDTO(memberMeeting.getMember())).collect(Collectors.toList());
        this.meetingPictureUrls = meeting.getMeetingPictureUrls();
        this.title = meeting.getTitle();
        this.description = meeting.getDescription();
        this.location = meeting.getLocation();
        this.capacity = meeting.getCapacity();
        this.meetingDate = meeting.getMeetingDate();
        this.isSharable = meeting.getIsSharable();
    }
}
