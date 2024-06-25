package kr.co.ssalon.web.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingHomeDTO {
    private String categoryName;
    private List<MeetingHomeSearchDTO> meetingList;
}
