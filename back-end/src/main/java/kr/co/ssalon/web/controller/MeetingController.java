package kr.co.ssalon.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.domain.service.MeetingService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.MeetingDTO;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.web.dto.MeetingSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "모임")
@Slf4j
@RestController
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @Operation(summary = "모임 참가")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "모임 참가 성공"),
    })
    @PostMapping("/moims/{moimId}/users")
    public ResponseEntity<?> joinMoim(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) {
        try {
            MeetingDTO joinedMoim = meetingService.join(customOAuth2Member, moimId);
            return ResponseEntity.ok().body(joinedMoim);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

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
