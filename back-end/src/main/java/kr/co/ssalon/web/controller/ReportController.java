package kr.co.ssalon.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

import java.util.List;

@Tag(name = "신고")
@Slf4j
@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportRepository reportRepository;
    private final MemberService memberService;
    private final ReportService reportService;
    private final MeetingService meetingService;

    @Operation(summary = "신고 목록 조회")
    @ApiResponse(responseCode = "200", description = "신고 목록 조회 성공", content = {
            @Content(schema = @Schema(implementation = ReportListSearchPageDTO.class))
    })
    @GetMapping("/api/report")
    public ResponseEntity<?> getMyReports(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, ReportSearchCondition reportSearchCondition, Pageable pageable) {
        try {
            String username = customOAuth2Member.getUsername();
            Member member = memberService.findMember(username);
            Page<Report> reports = reportService.getMyReports(member.getId(), reportSearchCondition, pageable);
            Page<ReportListSearchDTO> reportsDto = reports.map(report ->new ReportListSearchDTO(report));
            ReportListSearchPageDTO reportListSearchPageDTO = new ReportListSearchPageDTO(reportsDto);
            return ResponseEntity.ok().body(new JsonResult<>(reportListSearchPageDTO).getData());
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "신고 내용 조회")
    @ApiResponse(responseCode = "200", description = "신고 내용 조회 성공", content = {
            @Content(schema = @Schema(implementation = ReportDTO.class))
    })
    @GetMapping("/api/report/{reportId}")
    public ResponseEntity<?> getMyReports(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long reportId) {
        try {
            String username = customOAuth2Member.getUsername();
            validationReporter(reportId, username);
            Report report = reportService.findReport(reportId);
            ReportDTO reportDTO = new ReportDTO(report);
            return ResponseEntity.ok().body(new JsonResult<>(reportDTO).getData());
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "유저 신고")
    @ApiResponse(responseCode = "200", description = "유저 신고 성공", content = {
            @Content(schema = @Schema(implementation = ReportDTO.class))
    })
    @PostMapping("/api/report")
    public ResponseEntity<?> createReport(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @RequestBody ReportDTO reportDTO) {
        try {
            Member reportMember = memberService.findMember(customOAuth2Member.getUsername());
            Member reportedMember = memberService.findMember(reportDTO.getReportedUserId());
            Report report = Report.createReport(reportMember, reportedMember, reportDTO.getReason(), reportDTO.getReportPictureUrls());
            ReportDTO responseReportDTO = new ReportDTO(reportService.createReport(report));
            return ResponseEntity.ok().body(new JsonResult<>(responseReportDTO).getData());
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "유저 신고 수정")
    @ApiResponse(responseCode = "200", description = "유저 신고 수정 성공", content = {
            @Content(schema = @Schema(implementation = ReportDTO.class))
    })
    @PatchMapping("/api/report/{reportId}")
    public ResponseEntity<?> editReport(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long reportId, @RequestBody ReportDTO reportDTO) {
        try {
            String username = customOAuth2Member.getUsername();
            validationReporter(reportId, username);
            Report report = reportService.editReport(reportId, reportDTO);
            ReportDTO responseReportDTO = new ReportDTO(report);
            return ResponseEntity.ok().body(new JsonResult<>(responseReportDTO).getData());
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "유저 신고 삭제")
    @ApiResponse(responseCode = "200", description = "유저 신고 삭제 성공", content = {
            @Content(schema = @Schema(implementation = Long.class))
    })
    @DeleteMapping("/api/report/{reportId}")
    public ResponseEntity<?> deleteReport(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long reportId) {
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
