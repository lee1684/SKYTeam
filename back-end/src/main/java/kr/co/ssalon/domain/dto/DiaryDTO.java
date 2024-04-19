package kr.co.ssalon.domain.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiaryDTO {

    private Long id;
    private Long memberMeetingId;
    private String title;
    private String imageUrl;
    private String description;
}
