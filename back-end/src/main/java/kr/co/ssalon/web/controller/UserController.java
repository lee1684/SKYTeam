package kr.co.ssalon.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.domain.dto.MemberDomainDTO;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.JsonResult;
import kr.co.ssalon.web.dto.MemberDTO;
import kr.co.ssalon.web.dto.MemberSignDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "회원")
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final MemberService memberService;

    @Operation(summary = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
    })
    @PostMapping("/api/auth/signup")
    public JsonResult<MemberSignDTO> signup(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @RequestBody MemberDTO additionalInfo) throws BadRequestException {
        String username = customOAuth2Member.getUsername();
        MemberDomainDTO memberDomainDTO = new MemberDomainDTO(
                additionalInfo.getNickname(), additionalInfo.getProfilePictureUrl(),
                additionalInfo.getGender(), additionalInfo.getAddress(), additionalInfo.getIntroduction(),
                additionalInfo.getInterests(), additionalInfo.getBlackReason()
        );

        Member currentUser = memberService.signup(username, memberDomainDTO);
        MemberSignDTO memberSignDTO = new MemberSignDTO(currentUser);
        return new JsonResult<>(memberSignDTO);
    }

    @Operation(summary = "회원 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
    })
    @GetMapping("/api/users/me/profile")
    public MemberDTO getUserInfo(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) throws BadRequestException {
        String username = customOAuth2Member.getUsername();
        Member member = memberService.findMember(username);
        log.info("user = {}", member);
        return new MemberDTO(member);
    }

    @Operation(summary = "로그아웃")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
    })
    @DeleteMapping("/api/auth/logout")
    public void logout() {
        // swagger logout API 작성용으로 만든 빈 메소드
    }
}
