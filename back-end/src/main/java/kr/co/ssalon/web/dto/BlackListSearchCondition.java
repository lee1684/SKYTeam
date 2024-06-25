package kr.co.ssalon.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.ssalon.domain.dto.BlackListOrder;
import kr.co.ssalon.domain.dto.ReportOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "블랙리스트 조회 condition")
@Builder
public class BlackListSearchCondition {

    @Schema(description = "블랙 리스트 사유", example = "exist, null")
    private String blackReason;

    @Schema(description = "정렬", example = "NUMBER, RECENT, NAME")
    private BlackListOrder order;
}
