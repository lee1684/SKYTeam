package kr.co.ssalon.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.ssalon.domain.dto.MemberDomainDTO;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.repository.MemberRepository;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Tag(name = "회원")
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Operation(summary = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
    })
    @PostMapping("/api/auth/signup")
    public MemberSignDTO signup(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @RequestBody MemberDomainDTO additionalInfo) throws BadRequestException {
        String username = customOAuth2Member.getUsername();

        Member currentUser = memberService.signup(username, additionalInfo);
        MemberSignDTO memberSignDTO = new MemberSignDTO(currentUser);
        return new JsonResult<>(memberSignDTO).getData();
    }

    @Operation(summary = "회원가입 여부 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 여부 조회 성공"),
    })
    @GetMapping("/api/users/me/signup-verify")
    public MemberSignupVerificationDTO verifySignup(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) throws BadRequestException {
        String username = customOAuth2Member.getUsername();

        Member currentUser = memberService.findMember(username);
        Boolean isRegistered = currentUser.getNickname() != null;
        return new MemberSignupVerificationDTO(isRegistered);
    }

    @Operation(summary = "닉네임 중복 여부 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "닉네임 중복 여부 조회 성공"),
    })
    @PostMapping("/api/users/check-nickname")
    public Boolean checkNicknameDuplication(@RequestBody NicknameDTO nicknameDTO) {
        return memberService.checkNickname(nicknameDTO.getNickname());
    }

    @Operation(summary = "회원 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
    })
    @GetMapping("/api/users/me/profile")
    public MemberDomainDTO getUserInfo(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) throws BadRequestException {
        String username = customOAuth2Member.getUsername();
        Member member = memberService.findMember(username);
        log.info("user = {}", member);
        return new MemberDomainDTO(member);
    }

    @Operation(summary = "이메일로 회원 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일로 회원 정보 조회 성공"),
    })
    @GetMapping("/api/users/email/profile")
    public MemberDomainDTO getUserInfoByEmail(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @RequestBody MemberEmailDTO memberEmailDTO) throws BadRequestException {
        Member member = memberService.findMemberByEmail(memberEmailDTO.getEmail());
        return new MemberDomainDTO(member);
    }


    @Operation(summary = "회원 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공"),
    })
    @PatchMapping("/api/users/me/profile")
    public MemberDomainDTO update(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @RequestBody MemberDomainDTO additionalInfo) throws BadRequestException {
        String username = customOAuth2Member.getUsername();

        // 회원가입(/api/auth/signup) 로직과 일치함
        Member currentUser = memberService.signup(username, additionalInfo);
        MemberDomainDTO memberDomainDTO = new MemberDomainDTO(currentUser);
        return new JsonResult<>(memberDomainDTO).getData();
    }

    /*

    @Operation(summary = "참여한 모임 목록 조회")
    @ApiResponse(responseCode = "200", description = "참여한 모임 목록 조회 성공", content = {
            @Content(schema = @Schema(implementation = MeetingListSearchDTO.class))
    })
    @GetMapping("/api/users/moims/join")
    public ResponseEntity<?> getJoinedMeetingList(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            String username = customOAuth2Member.getUsername();
            List<MeetingListSearchDTO> joinedMeetingList = memberService.getJoinedMeetingList(username);
            return ResponseEntity.ok().body(joinedMeetingList);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "개최한 모임 목록 조회")
    @ApiResponse(responseCode = "200", description = "개최한 모임 목록 조회 성공", content = {
            @Content(schema = @Schema(implementation = MeetingListSearchDTO.class))
    })
    @GetMapping("/api/users/moims/create")
    public ResponseEntity<?> getCreatedMeetingList(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            String username = customOAuth2Member.getUsername();
            return ResponseEntity.ok().body(memberService.getCreatedMeetingLIst(username));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(summary = "진행 중인 내가 참여한 모임 목록 조회")
    @ApiResponse(responseCode = "200", description = "진행 중인 내가 참여한 모임 목록 조회", content = {
            @Content(schema = @Schema(implementation = MeetingListSearchDTO.class))
    })
    @GetMapping("/api/users/moims/unfinished")
    public ResponseEntity<?> getUnfinishedMeetingList(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            String username = customOAuth2Member.getUsername();
            return ResponseEntity.ok().body(memberService.getUnfinishedMeetingList(username));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "완료된 내가 참여한 모임 목록 조회")
    @ApiResponse(responseCode = "200", description = "완료된 내가 참여한 모임 목록 조회", content = {
            @Content(schema = @Schema(implementation = MeetingListSearchDTO.class))
    })
    @GetMapping("/api/users/moims/finished")
    public ResponseEntity<?> getFinishedMeetingList(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            String username = customOAuth2Member.getUsername();
            return ResponseEntity.ok().body(memberService.getFinishedMeetingList(username));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    */

    @Operation(summary = "로그아웃")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = {
            @Content(schema = @Schema(implementation = String.class))
    })
    @DeleteMapping("/api/auth/logout")
    public void logout() {
        // swagger logout API 작성용으로 만든 빈 메소드
    }

    @Operation(summary = "회원 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
    })
    @DeleteMapping("/api/users/me")
    public ResponseEntity<String> withdraw(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, HttpServletResponse response) throws BadRequestException {
        String username = customOAuth2Member.getUsername();
        memberService.withdraw(username);

        // access 쿠키 생성, 값은 빈 문자열로 설정하고, 만료 시간을 0으로 설정
        Cookie access = new Cookie("access", "");
        access.setMaxAge(0);
        access.setPath("/");
        response.addCookie(access);

        // refresh 쿠키 생성, 값은 빈 문자열로 설정하고, 만료 시간을 0으로 설정
        Cookie refresh = new Cookie("refresh", "");
        refresh.setMaxAge(0);
        refresh.setPath("/");
        response.addCookie(refresh);
        return ResponseEntity.ok("회원 탈퇴 성공");
    }
}
