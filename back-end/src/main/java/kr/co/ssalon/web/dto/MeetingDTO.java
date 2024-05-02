package kr.co.ssalon.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.MemberMeeting;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingDTO {

    @NotNull
    private Long id;
    @NotBlank
    private Long categoryId;

    private Long paymentId;
    @NotBlank
    private Long creatorId;
    @NotBlank
    private Long ticketId;
    private List<Long> participantIds;

    @NotNull
    private List<String> meetingPictureUrls;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String location;
    @NotBlank
    @Min(1)
    private Integer capacity;
    @NotBlank
    private LocalDateTime meetingDate;

    public MeetingDTO(Meeting meeting) {
        this.id = meeting.getId();
        this.categoryId = meeting.getCategory().getId();
        this.paymentId = meeting.getPayment().getId();
        this.creatorId = meeting.getCreator().getId();
        this.ticketId = meeting.getTicket().getId();
        this.participantIds = meeting.getParticipants().stream().map(MemberMeeting::getId).collect(Collectors.toList());
        this.meetingPictureUrls = meeting.getMeetingPictureUrls();
        this.title = meeting.getTitle();
        this.description = meeting.getDescription();
        this.location = meeting.getLocation();
        this.capacity = meeting.getCapacity();
        this.meetingDate = meeting.getMeetingDate();
    }

    public MeetingDTO(String test) {
        this.id = 1L;
        this.categoryId = 1L;
        this.paymentId = 1L;
        this.creatorId = 1L;
        this.ticketId = 1L;
        List<Long> participantIds = new ArrayList<>();
        participantIds.add(1L);
        participantIds.add(2L);
        this.participantIds = participantIds;
        List<String> meetingPictureUrls = new ArrayList<>();
        meetingPictureUrls.add("1234");
        meetingPictureUrls.add("abcde");
        this.meetingPictureUrls = meetingPictureUrls;
        this.title = test;
        this.description = test;
        this.location = test;
        this.capacity = 1;
        this.meetingDate = LocalDateTime.now();
    }
}
