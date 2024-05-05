package kr.co.ssalon.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "목록 조회 condition")
@Builder
public class MeetingSearchCondition {
    @Schema(description = "카테고리 이름",example = "운동, 공부, ... 등")
    private String category;
}
