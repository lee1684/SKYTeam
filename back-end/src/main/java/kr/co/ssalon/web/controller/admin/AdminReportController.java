package kr.co.ssalon.web.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.Report;
import kr.co.ssalon.domain.repository.ReportRepository;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.domain.service.ReportService;
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

@Tag(name = "관리자 신고")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminReportController {

    private final ReportService reportService;
    private final MemberService memberService;
    private final ValidationService validationService;
    private final ReportRepository reportRepository;


    // 필터링에 따라 조회하는거 필요
    @Operation(summary = "신고 내용 모두 조회")
    @ApiResponse(responseCode = "200", description = "신고 내용 모두 조회 성공", content = {
            @Content(schema = @Schema(implementation = ReportListSearchPageDTO.class))
    })
    @GetMapping("/api/admin/reports")
    public ResponseEntity<?> getReports(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, ReportSearchCondition reportSearchCondition, Pageable pageable) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);

            Page<Report> reports = reportService.getMyReports(null, reportSearchCondition, pageable);
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
    @GetMapping("/api/admin/report/{reportId}")
    public ResponseEntity<?> getReport(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long reportId) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);
            Report report = reportService.findReport(reportId);
            ReportDTO reportDTO = new ReportDTO(report);
            return ResponseEntity.ok().body(new JsonResult<>(reportDTO).getData());
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "신고 상태 처리")
    @ApiResponse(responseCode = "200", description = "신고 상태 처리 성공", content = {
            @Content(schema = @Schema(implementation = Boolean.class))
    })
    @PostMapping("/api/admin/report/{reportId}/state")
    public ResponseEntity<?> changeReportState(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long reportId) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);
            return ResponseEntity.ok().body(reportService.changeState(reportId));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "신고 삭제")
    @ApiResponse(responseCode = "200", description = "신고 삭제 성공", content = {
            @Content(schema = @Schema(implementation = Long.class))
    })
    @DeleteMapping("/api/admin/report/{reportId}")
    public ResponseEntity<?> deleteReport(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long reportId) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);
            reportRepository.deleteById(reportId);
            return ResponseEntity.ok().body(reportId);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private void validationAdmin(String username) throws BadRequestException {
        Member member = memberService.findMember(username);
        validationService.validationAdmin(member.getRole());
    }
}
