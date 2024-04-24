package kr.co.ssalon.web.controller.oauth2;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.hibernate.annotations.Bag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "oauth2 회원가입 완료 후 정보 출력 테스트입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
public class TestSignUpController {

    private final MemberService memberService;

    @Operation(summary = "회원정보 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "조회 성공"),
    })
    @GetMapping("/sign")
    public MemberSignTestDTO myAPI(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) throws BadRequestException {
        String username = customOAuth2Member.getUsername();
        log.info("username = {}",username);
        Member member = memberService.findMember(username);
        return new MemberSignTestDTO(member);
    }

    @Schema(description = "회원정보 테스트 DTO")
    @Data
    @AllArgsConstructor
    static class MemberSignTestDTO {
        @Schema(description = "고유 회원 이름",example = "naver 12314214124532")
        private String username;
        @Schema(description = "권한 정보",example = "ROLE_USER")
        private String role;
        public MemberSignTestDTO(Member member) {
            this.username = member.getUsername();
            this.role = member.getRole();
        }
    }
}
