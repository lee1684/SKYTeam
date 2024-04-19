package kr.co.ssalon.domain.controller;

import kr.co.ssalon.domain.service.MeetingService;
import kr.co.ssalon.domain.dto.MeetingForm;
import kr.co.ssalon.domain.dto.ResignReasonForm;
import kr.co.ssalon.domain.service.MemberService;
import lombok.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;
    private final MemberService memberService;

    // 모임 개설
    // 사용자 JWT, 개설할 모임의 정보
    // 성공/실패 여부, 개설된 모임의 ID
    @PostMapping("/moims")
    public void createMoim(MeetingForm meetingForm) throws Exception {
        System.out.println("MeetingController.createMoim");
        System.out.println("meetingForm = " + meetingForm);
        memberService.createMoim(meetingForm);
    }

    // 모임 정보 조회
    // 사용자 JWT
    // 성공/실패 여부, 모임 관련 정보 데이터
    // (소속 여부에 따라 변동)
    @GetMapping("/moims/{moimId}")
    public void getMoim(@PathVariable Long moimId) {
        System.out.println("MeetingController.getMoim");
        System.out.println("moimId = " + moimId);
        memberService.getMoim(moimId);
    }

    // 모임 정보 수정
    // 사용자 JWT, 수정할 모임의 정보
    // 성공/실패 여부
    @PutMapping("/moims/{moimId}")
    public void updateMoim(@PathVariable Long moimId, MeetingForm meetingForm) {
        System.out.println("MeetingController.updateMoim");
        System.out.println("moimId = " + moimId);
        System.out.println("meetingForm = " + meetingForm);
        memberService.updateMoim(moimId, meetingForm);
    }

    // 모임 해산
    // 사용자 JWT
    // 성공/실패 여부
    @DeleteMapping("/moims/{moimId}")
    public void deleteMoim(@PathVariable Long moimId) {
        System.out.println("MeetingController.deleteMoim");
        System.out.println("moimId = " + moimId);
        memberService.deleteMoim(moimId);
    }

    // 모임 참가자 목록 조회
    // 사용자 JWT
    // 성공/실패 여부, 모임 참가자 목록
    @GetMapping("/moims/{moimId}/users")
    public void getUsers(@PathVariable Long moimId, @RequestBody User user) {
        System.out.println("MeetingController.getUsers");
        System.out.println("moimId = " + moimId);
        memberService.getUsers(moimId);
    }

    // 모임 참가
    // 사용자 JWT
    // 성공/실패 여부
    @PostMapping("/moims/{moimId}/users")
    public void addUser(@PathVariable Long moimId, @RequestBody User user) {
        System.out.println("MeetingController.addUser");
        System.out.println("moimId = " + moimId);
        memberService.addUser(moimId);
    }

    // 모임 탈퇴/강퇴
    // 사용자 JWT, 탈퇴 또는 강퇴 사유
    // 성공/실패 여부
    @DeleteMapping("/moims/{moimId}/users/{userId}")
    public void deleteUser(@PathVariable Long moimId, @PathVariable Long userId, ResignReasonForm resignReasonForm) {
        System.out.println("MeetingController.deleteUser");
        System.out.println("moimId = " + moimId);
        System.out.println("userId = " + userId);
        System.out.println("resignReasonForm = " + resignReasonForm);
        memberService.deleteUser(moimId, userId, resignReasonForm);
    }
}