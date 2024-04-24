package kr.co.ssalon.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.ssalon.domain.entity.Diary;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiaryDTO {

    @NotNull
    private Long id;
    @NotBlank
    private Long memberMeetingId;
    @NotBlank
    private String title;
    @NotNull
    private String imageUrl;
    @NotBlank
    private String description;

    public DiaryDTO(Diary diary) {
        this.id = diary.getId();
        this.memberMeetingId = diary.getMemberMeeting().getId();
        this.title = diary.getTitle();
        this.imageUrl = diary.getImage_url();
        this.description = diary.getDescription();
    }

}
