package kr.co.ssalon.web.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.domain.entity.MeetingOut;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.repository.MeetingOutRepository;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.domain.service.ValidationService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.BlackListSearchDTO;
import kr.co.ssalon.web.dto.BlackListSearchPageDTO;
import kr.co.ssalon.web.dto.JsonResult;
import kr.co.ssalon.web.dto.MeetingOutDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "관리자 강퇴 및 탈퇴 사유 조회")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminMeetingOutController {

    private final MeetingOutRepository meetingOutRepository;
    private final ValidationService validationService;
    private final MemberService memberService;

    @Operation(summary = "관리자 강퇴 및 탈퇴 사유 조회")
    @ApiResponse(responseCode = "200", description = "관리자 강퇴 및 탈퇴 사유 조회 성공", content = {
            @Content(schema = @Schema(implementation = MeetingOutDTO.class))
    })
    @GetMapping("/api/admin/moims/meetingout")
    public ResponseEntity<?> getMeetingOuts(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            String username = customOAuth2Member.getUsername();
            // validationAdmin(username);
            List<MeetingOut> meetingOutList = meetingOutRepository.findAll();
            List<MeetingOutDTO> meetingOutDTOList = meetingOutList.stream()
                    .map(MeetingOutDTO::new)
                    .toList();
            return ResponseEntity.ok().body(new JsonResult(meetingOutDTOList).getData());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private void validationAdmin(String username) throws BadRequestException {
        Member member = memberService.findMember(username);
        validationService.validationAdmin(member.getRole());
    }
}
