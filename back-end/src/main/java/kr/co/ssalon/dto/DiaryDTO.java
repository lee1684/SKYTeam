package kr.co.ssalon.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaryDTO {
    private String id;
    private String title;
    private String image_url;
    private String description;
    private String decoration;
}
