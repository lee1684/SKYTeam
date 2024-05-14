package kr.co.ssalon.web.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "홈 화면 목록 조회 condition")
@Builder
public class HomeMeetingSearchCondition {
    @Schema(description = "조회할 상위 카테고리 숫자", example = "1, 2, 3, 4, ... ")
    private Long categoryLen;

    @Schema(description = "각 카테고리당 모임 개수")
    private Long meetingLen;

}
