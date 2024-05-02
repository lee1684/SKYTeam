package kr.co.ssalon.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingDomainDTO {

    private Long categoryId;
    private List<String> meetingPictureUrls;
    private String title;
    private String description;
    private String location;
    private Integer capacity;
    private LocalDateTime meetingDate;



}
