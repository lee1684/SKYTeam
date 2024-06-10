package kr.co.ssalon.web.dto;


import io.swagger.v3.oas.annotations.media.Schema;
<<<<<<< HEAD
import kr.co.ssalon.domain.dto.MeetingOrder;
=======
>>>>>>> develop
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
<<<<<<< HEAD
    private Integer categoryLen;

    @Schema(description = "조회할 카테고리 페이지 번호", example = "1, 2, 3, 4, ... ")
    private Integer categoryPage;

    @Schema(description = "각 카테고리당 모임 개수")
    private Integer meetingLen;

    @Schema(description = "isEnd 필터링", example = "true, false")
    private Boolean isEnd;

    @Schema(description = "정렬", example = "CAPACITY, NUMBER, RECENT. 각각 수용인원수, 모임번호, 최신순. 추후 추가 가능성 있음")
    private MeetingOrder order;
=======
    private Long categoryLen;

    @Schema(description = "각 카테고리당 모임 개수")
    private Long meetingLen;

    @Schema(description = "isEnd 필터링", example = "true, false")
    private Boolean isEnd;
>>>>>>> develop
}
