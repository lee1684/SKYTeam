package kr.co.ssalon.web.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.Report;
import kr.co.ssalon.domain.repository.MemberRepository;
import kr.co.ssalon.domain.service.MemberService;
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

@Tag(name = "관리자 블랙리스트")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminBlackListController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final ValidationService validationService;

    @Operation(summary = "블랙리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "블랙리스트 조회 성공"),
    })
    @GetMapping("/api/admin/blacklists/users")
    public ResponseEntity<?> getBlackList(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, BlackListSearchCondition blackListSearchCondition, Pageable pageable) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);

            Page<Member> members = memberService.getBlackList(blackListSearchCondition, pageable);
            Page<BlackListSearchDTO> membersDto = members.map(member -> new BlackListSearchDTO(member));
            BlackListSearchPageDTO blackListSearchPageDTO = new BlackListSearchPageDTO(membersDto);
            return ResponseEntity.ok().body(new JsonResult<>(blackListSearchPageDTO).getData());
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "블랙리스트 설정 및 해제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "블랙리스트 설정 및 해제 성공"),
    })
    @PostMapping("/api/admin/blacklists/users/{userId}")
    public ResponseEntity<?> setBlackReason(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long userId, @RequestBody BlackReasonDTO blackReasonDTO) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);
            return ResponseEntity.ok().body(memberService.changeBlackReason(userId, blackReasonDTO.getBlackReason()));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private void validationAdmin(String username) throws BadRequestException {
        Member member = memberService.findMember(username);
        validationService.validationAdmin(member.getRole());
    }
}
