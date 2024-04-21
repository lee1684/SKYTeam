package kr.co.ssalon.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.service.MeetingService;
import kr.co.ssalon.web.dto.MeetingDTO;
import kr.co.ssalon.web.dto.MeetingSearchCondition;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "")
@Slf4j
@RestController
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    // 모임 목록 조회
    // 모임 목록 필터 설정, 목록에 표시될 모임의 숫자 등
    // 현재 개설된 모임 목록

    @Operation(summary = "모임 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "조회 성공"),
    })
    @GetMapping("/moims")
    public ResponseEntity<Page<MeetingDTO>> getMoims(MeetingSearchCondition meetingSearchCondition, Pageable pageable) {
        Page<Meeting> moims = meetingService.getMoims(meetingSearchCondition, pageable);
        Page<MeetingDTO> moimsDto = moims.map(meeting -> new MeetingDTO(meeting));
        return ResponseEntity.ok().body(moimsDto);
    }


}
