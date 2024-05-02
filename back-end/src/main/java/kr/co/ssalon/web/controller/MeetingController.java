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

import java.util.List;

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
    @PostMapping("/api/moims/{moimId}/users")
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
            @ApiResponse(responseCode = "200",description = "모임 목록 조회 성공"),
    })
    @GetMapping("/api/moims")
    public ResponseEntity<List<MeetingDTO>> getMoims(MeetingSearchCondition meetingSearchCondition, Pageable pageable) {
        Page<Meeting> moims = meetingService.getMoims(meetingSearchCondition, pageable);
        Page<MeetingDTO> moimsDto = moims.map(meeting -> new MeetingDTO(meeting));
        return ResponseEntity.ok().body(moimsDto.getContent());
    }


    @Operation(summary = "테스트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "테스트 성공"),
    })
    @GetMapping("/api/test")
    public ResponseEntity<?> getTest(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        return ResponseEntity.ok().body("123");
    }

    // 모임 개설
    // 사용자 JWT, 개설할 모임의 정보
    // 성공/실패 여부, 개설된 모임의 ID
    @Operation(summary = "모임 개설")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "모임 개설 성공"),
    })
    @PostMapping("/moims")
    public ResponseEntity<?> createMoim(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @RequestBody MeetingDTO meetingDTO) {
        try {
            return ResponseEntity.ok().body(meetingService.createMoim(customOAuth2Member, meetingDTO));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 모임 정보 조회
    // 사용자 JWT
    // 성공/실패 여부, 모임 관련 정보 데이터
    // (소속 여부에 따라 변동) -> ?
    @Operation(summary = "모임 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "모임 정보 조회 성공"),
    })
    @GetMapping("/moims/{moimId}")
    public ResponseEntity<?> getMoim(@PathVariable Long moimId, @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            return ResponseEntity.ok().body(meetingService.getMoim(customOAuth2Member, moimId));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 모임 정보 수정
    // 사용자 JWT, 수정할 모임의 정보
    // 성공/실패 여부
    @Operation(summary = "모임 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "모임 정보 수정 성공"),
    })
    @PatchMapping("/moims/{moimId}")
    public ResponseEntity<?> updateMoim(@PathVariable Long moimId, @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, MeetingDTO meetingDTO) {
        try {
            return ResponseEntity.ok().body(meetingService.updateMoim(customOAuth2Member, moimId, meetingDTO));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 모임 해산
    // 사용자 JWT
    // 성공/실패 여부
    @Operation(summary = "모임 해산")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "모임 해산 성공"),
    })
    @DeleteMapping("/moims/{moimId}")
    public ResponseEntity<?> deleteMoim(@PathVariable Long moimId, @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            return ResponseEntity.ok().body(meetingService.deleteMoim(customOAuth2Member, moimId));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 모임 참가자 목록 조회
    // 사용자 JWT
    // 성공/실패 여부, 모임 참가자 목록
    @Operation(summary = "모임 참가자 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "모임 참가자 목록 조회"),
    })
    @GetMapping("/moims/{moimId}/users")
    public ResponseEntity<?> getUsers(@PathVariable Long moimId, @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            return ResponseEntity.ok().body(meetingService.getUsers(customOAuth2Member, moimId));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
