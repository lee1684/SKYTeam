package kr.co.ssalon.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.ssalon.domain.entity.Region;
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
    @Schema(description = "카테고리명",example = "운동, 스터디 등")
    private String categoryName;
    @Schema(description = "지역",example = "SEOUL, GYEONGGIDO 등")
    private Region region;
}
