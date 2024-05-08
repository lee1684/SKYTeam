package kr.co.ssalon.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.domain.entity.Category;
import kr.co.ssalon.domain.service.AttendanceService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "출석")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Operation(summary = "모임 참가자 출석 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모임 참가자 출석 목록 조회 성공"),
    })
    @GetMapping("/api/moims/{moimId}/attendance")
    public ResponseEntity<?> getAttendances(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) {
        try {
            return ResponseEntity.ok().body(attendanceService.getAttendances(moimId));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "출석 상태 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "출석 상태 변경 성공"),
    })
    @PostMapping("/api/moims/{moimId}/attendance/{userId}")
    public ResponseEntity<?> changeAttendance(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId, @PathVariable Long userId) {
        try {
            return ResponseEntity.ok().body(attendanceService.changeAttendance(moimId, userId));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
