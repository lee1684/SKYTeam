package kr.co.ssalon.domain.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingDTO {

    private Long id;
    private Long categoryId;
    private Long paymentId;
    private Long creatorId;
    private Long ticketId;
    private List<Long> participantIds;
    private List<String> meetingPictureUrls;
    private String title;
    private String description;
    private String location;
    private Integer capacity;
    private LocalDateTime meetingDate;
}
