package kr.co.ssalon.web.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.Report;
import kr.co.ssalon.domain.repository.ReportRepository;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.domain.service.ReportService;
import kr.co.ssalon.domain.service.ValidationService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.ReportDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
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
    @Operation(summary = "신고 내용 모두 조회하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신고 내용 모두 조회하기 성공"),
    })
    @GetMapping("/api/admin/reports")
    public ResponseEntity<?> getReports(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);
            return ResponseEntity.ok().body(reportRepository.findAll());
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "신고 내용 조회하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "신고 내용 조회하기 성공"),
    })
    @GetMapping("/api/admin/report/{reportId}")
    public ResponseEntity<?> getReport(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long reportId) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);
            return ResponseEntity.ok().body(reportService.findReport(reportId));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }




    private void validationAdmin(String username) throws BadRequestException {
        Member member = memberService.findMember(username);
        validationService.validationAdmin(member.getRole());
    }
}
