package kr.co.ssalon.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.domain.entity.Category;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.Report;
import kr.co.ssalon.domain.repository.ReportRepository;
import kr.co.ssalon.domain.service.MeetingService;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.domain.service.ReportService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.CategoryDTO;
import kr.co.ssalon.web.dto.ReportDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "신고")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportRepository reportRepository;
    private final MemberService memberService;
    private final ReportService reportService;
    private final MeetingService meetingService;

    @Operation(summary = "자신의 신고한 목록 조회하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자신의 신고한 목록 조회하기"),
    })
    @GetMapping("/api/report")
    public ResponseEntity<?> getMyReports(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            String username = customOAuth2Member.getUsername();
            Member member = memberService.findMember(username);
            return ResponseEntity.ok().body(reportService.getMyReports(member.getId()));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "자신의 신고 내용 조회하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "자신의 신고 내용 조회하기"),
    })
    @GetMapping("/api/report/{reportId}")
    public ResponseEntity<?> getMyReports(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long reportId) {
        try {
            String username = customOAuth2Member.getUsername();
            validationReporter(reportId, username);
            return ResponseEntity.ok().body(reportService.findReport(reportId));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "유저 신고하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 신고하기 성공"),
    })
    @PostMapping("/api/report")
    public ResponseEntity<?> createReport(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @RequestBody ReportDTO reportDTO) {
        try {
            Member reportMember = memberService.findMember(customOAuth2Member.getUsername());
            Member reportedMember = memberService.findMember(reportDTO.getReportedUserId());
            Report report = Report.createReport(reportMember, reportedMember, reportDTO.getReason());
            return ResponseEntity.ok().body(reportService.createReport(report));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "유저 신고 수정하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 신고 수정하기 성공"),
    })
    @PatchMapping("/api/report/{reportId}")
    public ResponseEntity<?> editReport(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long reportId, @RequestBody ReportDTO reportDTO) {
        try {
            String username = customOAuth2Member.getUsername();
            validationReporter(reportId, username);
            return ResponseEntity.ok().body(reportService.editReport(reportId, reportDTO));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "신고 삭제하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신고 삭제하기 성공"),
    })
    @DeleteMapping("/api/report/{reportId}")
    public ResponseEntity<?> deleteReport(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long reportId, @RequestBody ReportDTO reportDTO) {
        try {
            String username = customOAuth2Member.getUsername();
            validationReporter(reportId, username);
            reportRepository.deleteById(reportId);
            return ResponseEntity.ok().body(reportId);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private void validationReporter(Long reportId, String username) throws BadRequestException {
        Member member = memberService.findMember(username);
        Report report = reportService.findReport(reportId);
        if (member.equals(report.getReporter())) {
        } else {
            throw new BadRequestException("자신이 신고한 내용이 아닙니다.");
        }
    }
}
