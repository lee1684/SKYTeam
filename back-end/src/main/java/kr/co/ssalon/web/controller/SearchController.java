package kr.co.ssalon.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.service.MeetingService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.JsonResult;
import kr.co.ssalon.web.dto.MeetingListSearchDTO;
import kr.co.ssalon.web.dto.MeetingListSearchPageDTO;
import kr.co.ssalon.web.dto.MeetingSearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "검색")
@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchController {

    private final MeetingService meetingService;

    @Operation(summary = "키워드로 모임 리스트 조회")
    @ApiResponse(responseCode = "200", description = "키워드로 모임 리스트 조회 성공", content = {
            @Content(schema = @Schema(implementation = MeetingListSearchPageDTO.class))
    })
    @GetMapping("/api/moims/search/keyword")
    public ResponseEntity searchTitleByKeyword(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @RequestParam(value ="keyword",required = false) String keyword, Pageable pageable){
        String username = customOAuth2Member.getUsername();
        Page<Meeting> moims = meetingService.searchByKeyword(keyword, pageable);
        Page<MeetingListSearchDTO> moimsDto = moims.map(meeting -> new MeetingListSearchDTO(meeting, username));
        MeetingListSearchPageDTO meetingListSearchPageDTO = new MeetingListSearchPageDTO(moimsDto);
        return ResponseEntity.ok().body(new JsonResult<>(meetingListSearchPageDTO).getData());
    }
}
