package kr.co.ssalon.dto;

import kr.co.ssalon.domain.entity.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeetingDTO {
    private Long id;
    private Category category;
    private Payment payment;
    private Member creator;
    private Ticket ticket;
    private List<MemberMeeting> participants;
    private List<String> meetingPictureUrls;
    private String title;
    private String description;
    private String location;
    private Integer capacity;
    private LocalDateTime meetingDate;
}
