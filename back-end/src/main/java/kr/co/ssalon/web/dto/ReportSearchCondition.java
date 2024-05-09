package kr.co.ssalon.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import kr.co.ssalon.domain.dto.ReportOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "신고 목록 조회 condition")
@Builder
public class ReportSearchCondition {
    @Schema(description = "신고 처리 여부", example = "true, false")
    private Boolean isSolved;

    @Schema(description = "정렬", example = "RECENT, NUMBER, 신고번호. 추후 추가 가능성 있음")
    private ReportOrder order;
}




