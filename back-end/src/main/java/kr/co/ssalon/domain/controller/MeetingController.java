package kr.co.ssalon.domain.controller;

import jakarta.validation.Valid;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.service.MeetingService;
import kr.co.ssalon.domain.dto.MeetingForm;
import kr.co.ssalon.domain.dto.ResignReasonForm;
import kr.co.ssalon.domain.service.MemberMeetingService;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;
    private final MemberService memberService;
    private final MemberMeetingService memberMeetingService;

    // 모임 개설
    // 사용자 JWT, 개설할 모임의 정보
    // 성공/실패 여부, 개설된 모임의 ID
    @PostMapping("/moims")
    public ResponseEntity<Long> createMoim(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @RequestBody MeetingForm meetingForm) {
        String username = customOAuth2Member.getUsername();
        Member member = memberService.findMember(username);

        Meeting meeting = Meeting.createMeeting(member, meetingForm);

        return ResponseEntity.ok(meetingService.createMoim(member, meeting));
    }

    // 모임 정보 조회
    // 사용자 JWT
    // 성공/실패 여부, 모임 관련 정보 데이터
    // (소속 여부에 따라 변동) -> ?
    @GetMapping("/moims/{moimId}")
    public ResponseEntity<Meeting> getMoim(@PathVariable Long moimId, @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        String username = customOAuth2Member.getUsername();
        Member member = memberService.findMember(username);

        Boolean isParticipant = meetingService.isParticipant(moimId, member);

        // 단순히 현재 Meeting Entity field에서는 소속이 아닐때 전달하지 말아야할 필드값이 존재하지 않음
        return ResponseEntity.ok(meetingService.getMoim(isParticipant, moimId));
    }

    // 모임 정보 수정
    // 사용자 JWT, 수정할 모임의 정보
    // 성공/실패 여부
    @PatchMapping("/moims/{moimId}")
    public ResponseEntity<Void> updateMoim(@PathVariable Long moimId, @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, MeetingForm meetingForm) {
        String username = customOAuth2Member.getUsername();
        Member member = memberService.findMember(username);

        Meeting meeting = Meeting.createMeeting(member, meetingForm);
        meetingService.updateMoim(moimId, member, meeting);

        return ResponseEntity.ok(null);
    }

    // 모임 해산
    // 사용자 JWT
    // 성공/실패 여부
    @DeleteMapping("/moims/{moimId}")
    public ResponseEntity<Void> deleteMoim(@PathVariable Long moimId, @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        String username = customOAuth2Member.getUsername();
        Member member = memberService.findMember(username);

        meetingService.deleteMoim(moimId, member);

        return ResponseEntity.ok(null);
    }

    // 모임 참가자 목록 조회
    // 사용자 JWT
    // 성공/실패 여부, 모임 참가자 목록
    @GetMapping("/moims/{moimId}/users")
    public ResponseEntity<List<Member>> getUsers(@PathVariable Long moimId, @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
//        String username = customOAuth2Member.getUsername();
//        Member member = memberService.findMember(username);
//        Long userId = member.getId();

        return ResponseEntity.ok(meetingService.getUsers(moimId));
    }

    // 모임 참가
    // 사용자 JWT
    // 성공/실패 여부
    @PostMapping("/moims/{moimId}/users")
    public ResponseEntity<Void> addUser(@PathVariable Long moimId, @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        String username = customOAuth2Member.getUsername();
        Member member = memberService.findMember(username);

        meetingService.addUser(moimId, member);

        return ResponseEntity.ok(null);
    }

    // 모임 탈퇴/강퇴
    // 사용자 JWT, 탈퇴 또는 강퇴 사유
    // 성공/실패 여부
//    @PutMapping("/moims/{moimId}/users/{memberId}")
//    public ResponseEntity<Void> deleteMemberMeeting(@PathVariable Long moimId, @PathVariable Long memberId, @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, ResignReasonForm resignReasonForm) {
//        String username = customOAuth2Member.getUsername();
//        Member member = memberService.findMember(username);
//        Long userId = member.getId();
//
//        memberMeetingService.deleteMemberMeeting(moimId, memberId, userId, resignReasonForm);
//
//        return ResponseEntity.ok(null);
//    }
}