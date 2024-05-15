package kr.co.ssalon.web.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.domain.dto.MemberDomainDTO;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.domain.service.ValidationService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.MeetingOutDTO;
import kr.co.ssalon.web.dto.MemberSignDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "관리자 유저")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminMemberController {

    private final MemberService memberService;
    private final ValidationService validationService;

    @Operation(summary = "사용자 목록 조회")
    @ApiResponse(responseCode = "200", description = "사용자 목록 조회 성공", content = {
            @Content(schema = @Schema(implementation = Member.class))
    })
    @GetMapping("/api/admin/users")
    public List<MemberSignDTO> getUserList(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) throws BadRequestException {
        String username = customOAuth2Member.getUsername();
        validationAdmin(username);

        return memberService.findAllMember().stream().map(MemberSignDTO::new).collect(Collectors.toList());
    }

    @Operation(summary = "특정 사용자 정보 조회")
    @ApiResponse(responseCode = "200", description = "특정 사용자 정보 조회 성공", content = {
            @Content(schema = @Schema(implementation = MemberSignDTO.class))
    })
    @GetMapping("/api/admin/users/{userId}")
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long userId) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);
            Member member = memberService.findMember(userId);

            return ResponseEntity.ok(new MemberSignDTO(member));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "특정 사용자 정보 수정")
    @ApiResponse(responseCode = "200", description = "특정 사용자 정보 수정 성공", content = {
            @Content(schema = @Schema(implementation = MemberSignDTO.class))
    })
    @PatchMapping("/api/admin/users/{userId}")
    public ResponseEntity<?> updateUserInfo(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long userId, @RequestBody MemberDomainDTO additionalInfo) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);

            Member updateUser = memberService.updateUserInfoForAdmin(userId, additionalInfo);
            return ResponseEntity.ok(new MemberSignDTO(updateUser));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "특정 사용자 탈퇴 처리")
    @ApiResponse(responseCode = "200", description = "특정 사용자 탈퇴 처리 성공", content = {
            @Content(schema = @Schema(implementation = String.class))
    })
    @DeleteMapping("/api/admin/users/{userId}")
    public ResponseEntity<?> withdraw(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long userId) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);

            // 탈퇴 시킬 회원 찾기
            Member withdrawMember = memberService.findMember(userId);

            // 관리자 회원 탈퇴 시도 예외 처리
            if (username.equals(withdrawMember.getUsername())) {
                return new ResponseEntity<>("관리자는 탈퇴시킬 수 없습니다.", HttpStatus.BAD_REQUEST);
            }

            memberService.withdraw(withdrawMember.getUsername());
            return ResponseEntity.ok("회원 탈퇴 성공");
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private void validationAdmin(String username) throws BadRequestException {
        Member member = memberService.findMember(username);
        validationService.validationAdmin(member.getRole());
    }
}
