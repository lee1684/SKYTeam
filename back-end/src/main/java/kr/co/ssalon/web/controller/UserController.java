package kr.co.ssalon.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "회원가입 API")
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final MemberService memberService;

    @Operation(summary = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
    })
    @PostMapping("/auth/signup")
    public MemberDTO signup(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @RequestBody MemberDTO additionalInfo) throws BadRequestException {
        String username = customOAuth2Member.getUsername();
        Member currentUser = memberService.signup(username, additionalInfo);

        return new MemberDTO(currentUser);
    }
}
