package kr.co.ssalon.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.ssalon.domain.dto.MeetingOrder;
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
    @Schema(description = "카테고리 이름", example = "운동, 독서, 요리, 여행, 음악, 스터디, 쇼핑, 예술, 사진, 게임")
    private String category;

    @Schema(description = "isEnd 필터링", example = "true, false")
    private Boolean isEnd;

    @Schema(description = "내가 속한 여부 필터링", example = "true, false")
    private Boolean isParticipant;

    @Schema(description = "정렬", example = "CAPACITY, NUMBER, RECENT. 각각 수용인원수, 모임번호, 최신순. 추후 추가 가능성 있음")
    private MeetingOrder order;
}
