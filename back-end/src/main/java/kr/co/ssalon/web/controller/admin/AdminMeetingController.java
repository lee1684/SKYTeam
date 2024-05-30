package kr.co.ssalon.web.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.domain.dto.MeetingDomainDTO;
import kr.co.ssalon.domain.entity.Category;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.repository.MeetingRepository;
import kr.co.ssalon.domain.service.MeetingService;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.domain.service.ValidationService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자 모임")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminMeetingController {

    private final ValidationService validationService;
    private final MemberService memberService;
    private final MeetingRepository meetingRepository;
    private final MeetingService meetingService;


    @Operation(summary = "모임 전체 조회")
    @ApiResponse(responseCode = "200", description = "모임 전체 조회 성공", content = {
            @Content(schema = @Schema(implementation = Meeting.class))
    })
    @GetMapping("/api/admin/moims/all")
    public ResponseEntity<?> getMoimsAll(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);
            return ResponseEntity.ok().body(meetingRepository.findAll());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "모임 목록 조회")
    @ApiResponse(responseCode = "200", description = "모임 목록 조회 성공", content = {
            @Content(schema = @Schema(implementation = MeetingListSearchPageDTO.class))
    })
    @GetMapping("/api/admin/moims")
    public ResponseEntity<MeetingListSearchPageDTO> getMoims(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, MeetingSearchCondition meetingSearchCondition, Pageable pageable) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);
            Page<Meeting> moims = meetingService.getMoims(meetingSearchCondition, username, pageable);
            Page<MeetingListSearchDTO> moimsDto = moims.map(meeting -> new MeetingListSearchDTO(meeting, username));
            MeetingListSearchPageDTO meetingListSearchPageDTO = new MeetingListSearchPageDTO(moimsDto);
            return ResponseEntity.ok().body(new JsonResult<>(meetingListSearchPageDTO).getData());
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "특정 모임 정보 조회")
    @ApiResponse(responseCode = "200", description = "특정 모임 정보 조회 성공", content = {
            @Content(schema = @Schema(implementation = Meeting.class))
    })
    @GetMapping("/api/admin/moims/{moimId}")
    public ResponseEntity<?> getMoim(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);
            return ResponseEntity.ok().body(meetingService.findMeeting(moimId));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "모임 정보 수정")
    @ApiResponse(responseCode = "200", description = "모임 정보 수정 성공", content = {
            @Content(schema = @Schema(implementation = Long.class))
    })
    @PatchMapping("/api/admin/moims/{moimId}")
    public ResponseEntity<?> updateMoim(@PathVariable Long moimId, @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @RequestBody MeetingInfoDTO meetingInfoDTO) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);
            return ResponseEntity.ok().body(meetingService.editMoim(moimId, meetingInfoDTO));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "모임 해산")
    @ApiResponse(responseCode = "200", description = "모임 해산 성공", content = {
            @Content(schema = @Schema(implementation = Long.class))
    })
    @DeleteMapping("/api/admin/moims/{moimId}")
    public ResponseEntity<?> deleteMoim(@PathVariable Long moimId, @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);
            return ResponseEntity.ok().body(meetingService.deleteMoim(moimId));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private void validationAdmin(String username) throws BadRequestException {
        Member member = memberService.findMember(username);
        validationService.validationAdmin(member.getRole());
    }
}
