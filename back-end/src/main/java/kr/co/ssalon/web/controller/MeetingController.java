package kr.co.ssalon.web.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jdi.LongValue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.co.ssalon.domain.dto.MeetingDomainDTO;
import kr.co.ssalon.domain.entity.*;
import kr.co.ssalon.domain.entity.MeetingOut;
import kr.co.ssalon.domain.repository.CategoryRepository;
import kr.co.ssalon.domain.repository.MeetingRepository;
import kr.co.ssalon.domain.service.CategoryService;
import kr.co.ssalon.domain.service.MeetingService;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.domain.service.RecommendService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "모임")
@Slf4j
@RestController
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;
    private final MemberService memberService;
    private final MeetingRepository meetingRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final RecommendService recommendService;

    @Operation(summary = "모임 참가")
    @ApiResponse(responseCode = "200", description = "모임 참가 성공", content = {
            @Content(schema = @Schema(implementation = MeetingDTO.class))
    })
    @PostMapping("/api/moims/{moimId}/users")
    public ResponseEntity<?> joinMoim(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) {
        try {
            String username = customOAuth2Member.getUsername();
            meetingService.join(username, moimId);
            Meeting meeting = meetingService.findMeeting(moimId);
            MeetingDTO joinedMoim = new MeetingDTO(meeting);
            return ResponseEntity.ok().body(joinedMoim);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 모임 목록 조회
    // 모임 목록 필터 설정, 목록에 표시될 모임의 숫자 등
    // 현재 개설된 모임 목록
    @Operation(summary = "모임 목록 조회")
    @ApiResponse(responseCode = "200", description = "모임 목록 조회 성공", content = {
            @Content(schema = @Schema(implementation = MeetingListSearchPageDTO.class))
    })
    @GetMapping("/api/moims")
    public ResponseEntity<MeetingListSearchPageDTO> getMoims(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, MeetingSearchCondition meetingSearchCondition, Pageable pageable) throws BadRequestException {
        String username = customOAuth2Member.getUsername();
        Page<Meeting> moims = meetingService.getMoims(meetingSearchCondition, username, pageable);
        Page<MeetingListSearchDTO> moimsDto = moims.map(meeting -> new MeetingListSearchDTO(meeting, username));
        MeetingListSearchPageDTO meetingListSearchPageDTO = new MeetingListSearchPageDTO(moimsDto);
        return ResponseEntity.ok().body(new JsonResult<>(meetingListSearchPageDTO).getData());
    }

    @Operation(summary = "추천 모임 리스트 조회")
    @ApiResponse(responseCode = "200", description = "추천 모임 리스트 조회 성공", content = {
            @Content(schema = @Schema(implementation = MeetingListSearchDTO.class))
    })
    @GetMapping("/api/moims/recommend")
    public ResponseEntity<?> getMoimsByRecommend(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            Gson gson = new Gson();

            List<MeetingListSearchDTO> meetingListSearchDTOs = new ArrayList<>();
            String username = customOAuth2Member.getUsername();
            Member member = memberService.findMember(username);

            List<Long> meetingRecommendList = gson.fromJson(member.getMeetingRecommendation(), new TypeToken<List<Long>>() {});

            for (int i = 0; i < meetingRecommendList.size(); i++) {
                if (meetingRecommendList != null) {
                    Meeting meeting = meetingService.findMeeting(meetingRecommendList.get(i));
                    MeetingListSearchDTO meetingListSearchDTO = new MeetingListSearchDTO(meeting, username);
                    meetingListSearchDTOs.add(meetingListSearchDTO);
                } else {
                    Meeting meeting = meetingService.findMeeting((long) (i));
                    MeetingListSearchDTO meetingListSearchDTO = new MeetingListSearchDTO(meeting, username);
                    meetingListSearchDTOs.add(meetingListSearchDTO);
                }
            }

            return ResponseEntity.ok().body(new JsonResult<>(meetingListSearchDTOs).getData());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 홈 화면 조회
    // 모임 목록 필터 설정, 목록에 표시될 모임의 숫자 등
    // 현재 개설된 모임 목록
    @Operation(summary = "홈 화면 조회")
    @ApiResponse(responseCode = "200", description = "홈 화면 조회 성공", content = {
            @Content(schema = @Schema(implementation = MeetingListSearchPageDTO.class))
    })
    @GetMapping("/api/moims/home")
    public ResponseEntity<?> getHomeMoims(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, HomeMeetingSearchCondition homeMeetingSearchCondition) {
        try {
            Gson gson = new Gson();

            List<MeetingHomeDTO> categorizedMeetings = new ArrayList<>();
            String username = customOAuth2Member.getUsername();
            Member member = memberService.findMember(username);
            List<Long> categoryRecommendList = gson.fromJson(member.getCategoryRecommendation(), new TypeToken<List<Long>>() {});

            for (int i = 0; i < homeMeetingSearchCondition.getCategoryLen(); i++) {
                try {
                    if (categoryRecommendList != null) {
                        Integer index = homeMeetingSearchCondition.getCategoryLen() * homeMeetingSearchCondition.getCategoryPage() - 1;

                        String categoryName = categoryService.findCategory(categoryRecommendList.get(i + index)).getName();
                        List<Meeting> meetings = meetingRepository.findMeetingsByCategoryId(categoryRecommendList.get(i + index)).stream()
                                .filter(meeting -> {
                                    if (homeMeetingSearchCondition.getIsEnd() != null) {
                                        return homeMeetingSearchCondition.getIsEnd().equals(meeting.getIsFinished());
                                    }
                                    return true; // isEnd 필터가 없는 경우 모든 모임 포함
                                })
                                .sorted((meeting1, meeting2) -> {
                                    if (homeMeetingSearchCondition.getOrder() != null) {
                                        switch (homeMeetingSearchCondition.getOrder()) {
                                            case CAPACITY:
                                                return Integer.compare(meeting2.getCapacity(), meeting1.getCapacity());
                                            case NUMBER:
                                                return Long.compare(meeting2.getId(), meeting1.getId());
                                            case RECENT:
                                                return meeting2.getMeetingDate().compareTo(meeting1.getMeetingDate());
                                            default:
                                                return 0;
                                        }
                                    }
                                    return 0;
                                })
                                .limit(homeMeetingSearchCondition.getMeetingLen()) // 각 카테고리당 모임 최대 개수 설정
                                .collect(Collectors.toList());
                        MeetingHomeDTO meetingHomeDTO = new MeetingHomeDTO(categoryName, meetings.stream()
                                .map(meeting -> new MeetingHomeSearchDTO(meeting, username))
                                .collect(Collectors.toList()));
                        categorizedMeetings.add(meetingHomeDTO);
                    } else {
                        Integer index = homeMeetingSearchCondition.getCategoryLen() * homeMeetingSearchCondition.getCategoryPage() - 1;

                        String categoryName = categoryService.findCategory((long) (i + 1 + index)).getName();
                        List<Meeting> meetings = meetingRepository.findMeetingsByCategoryId((long) (i + 1 + index)).stream()
                                .filter(meeting -> {
                                    if (homeMeetingSearchCondition.getIsEnd() != null) {
                                        return homeMeetingSearchCondition.getIsEnd().equals(meeting.getIsFinished());
                                    }
                                    return true; // isEnd 필터가 없는 경우 모든 모임 포함
                                })
                                .sorted((meeting1, meeting2) -> {
                                    if (homeMeetingSearchCondition.getOrder() != null) {
                                        switch (homeMeetingSearchCondition.getOrder()) {
                                            case CAPACITY:
                                                return Integer.compare(meeting2.getCapacity(), meeting1.getCapacity());
                                            case NUMBER:
                                                return Long.compare(meeting2.getId(), meeting1.getId());
                                            case RECENT:
                                                return meeting2.getMeetingDate().compareTo(meeting1.getMeetingDate());
                                            default:
                                                return 0;
                                        }
                                    }
                                    return 0;
                                })
                                .limit(homeMeetingSearchCondition.getMeetingLen()) // 각 카테고리당 모임 최대 개수 설정
                                .collect(Collectors.toList());
                        MeetingHomeDTO meetingHomeDTO = new MeetingHomeDTO(categoryName, meetings.stream()
                                .map(meeting -> new MeetingHomeSearchDTO(meeting, username))
                                .collect(Collectors.toList()));
                        categorizedMeetings.add(meetingHomeDTO);
                    }
                } catch (BadRequestException e) {
                    // 카테고리가 없는 경우 해당 카테고리를 무시하고 다음 카테고리를 조회
                    continue;
                }
            }
            Boolean bool = categoryRepository.existsById((long) homeMeetingSearchCondition.getCategoryLen() * homeMeetingSearchCondition.getCategoryPage());
            MeetingListSearchPageDTO meetingListSearchPageDTO = new MeetingListSearchPageDTO(categorizedMeetings, bool);
            return ResponseEntity.ok().body(new JsonResult<>(meetingListSearchPageDTO).getData());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 모임 개설
    // 사용자 JWT, 개설할 모임의 정보
    // 성공/실패 여부, 개설된 모임의 ID
    @Operation(summary = "모임 개설")
    @ApiResponse(responseCode = "200", description = "모임 개설 성공", content = {
            @Content(schema = @Schema(implementation = MeetingDTO.class))
    })
    @PostMapping("/api/moims")
    public ResponseEntity<?> createMoim(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @RequestBody MeetingDomainDTO meetingDomainDTO) {
        try {
            String username = customOAuth2Member.getUsername();
            Long moimId = meetingService.createMoim(username, meetingDomainDTO);
            Meeting meeting = meetingService.findMeeting(moimId);
            MeetingDTO sendMeetingDTO = new MeetingDTO(meeting);
            return ResponseEntity.ok().body(new JsonResult<>(sendMeetingDTO).getData());
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 모임 정보 조회
    // 사용자 JWT
    // 성공/실패 여부, 모임 관련 정보 데이터
    // (소속 여부에 따라 변동) -> ?
    @Operation(summary = "모임 정보 조회")
    @ApiResponse(responseCode = "200", description = "모임 정보 조회 성공", content = {
            @Content(schema = @Schema(implementation = MeetingInfoDTO.class))
    })
    @GetMapping("/api/moims/{moimId}")
    public ResponseEntity<?> getMoim(@PathVariable Long moimId, @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            Meeting moim = meetingService.findMeeting(moimId);
            MeetingInfoDTO sendMeetingInfoDTO = new MeetingInfoDTO(moim);
            return ResponseEntity.ok().body(new JsonResult<>(sendMeetingInfoDTO).getData());
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 모임 정보 수정
    // 사용자 JWT, 수정할 모임의 정보
    // 성공/실패 여부
    @Operation(summary = "모임 정보 수정")
    @ApiResponse(responseCode = "200", description = "모임 정보 수정 성공", content = {
            @Content(schema = @Schema(implementation = Long.class))
    })
    @PatchMapping("/api/moims/{moimId}")
    public ResponseEntity<?> updateMoim(@PathVariable Long moimId, @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @RequestBody MeetingInfoDTO meetingInfoDTO) {
        try {
            String username = customOAuth2Member.getUsername();
            return ResponseEntity.ok().body(meetingService.editMoim(username, moimId, meetingInfoDTO));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 모임 해산
    // 사용자 JWT
    // 성공/실패 여부
    @Operation(summary = "모임 해산")
    @ApiResponse(responseCode = "200", description = "모임 해산 성공", content = {
            @Content(schema = @Schema(implementation = Long.class))
    })
    @DeleteMapping("/api/moims/{moimId}")
    public ResponseEntity<?> deleteMoim(@PathVariable Long moimId, @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            String username = customOAuth2Member.getUsername();
            return ResponseEntity.ok().body(meetingService.deleteMoim(username, moimId));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 모임 참가자 목록 조회
    // 사용자 JWT
    // 성공/실패 여부, 모임 참가자 목록
    @Operation(summary = "모임 참가자 목록 조회")
    @ApiResponse(responseCode = "200", description = "모임 참가자 목록 조회 성공", content = {
            @Content(schema = @Schema(implementation = ParticipantDTO.class))
    })
    @GetMapping("/api/moims/{moimId}/users")
    public ResponseEntity<?> getUsers(@PathVariable Long moimId, @AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            return ResponseEntity.ok().body(meetingService.getUsers(moimId));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // 모임 강퇴 및 탈퇴
    // 사용자 JWT, 탈퇴 및 강퇴 사유
    // 성공/실패 여부
    @Operation(summary = "모임 강퇴 및 탈퇴")
    @ApiResponse(responseCode = "200", description = "모임 강퇴 및 탈퇴 성공", content = {
            @Content(schema = @Schema(implementation = MeetingOutDTO.class))
    })
    @DeleteMapping("/api/moims/{moimId}/users/{userId}")
    public ResponseEntity<?> deleteUserFromMoim(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId, @PathVariable Long userId, @RequestBody MeetingOutReasonDTO meetingOutReasonDTO) {
        try{
            String username = customOAuth2Member.getUsername();
            MeetingOut meetingOut = meetingService.deleteUserFromMoim(username, moimId, userId, meetingOutReasonDTO.getReason());
            MeetingOutDTO meetingOutDTO = new MeetingOutDTO(meetingOut);
            return ResponseEntity.ok().body(new JsonResult<>(meetingOutDTO).getData());
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(summary = "모임 개최자 검증")
    @ApiResponse(responseCode = "200", description = "모임 개최자 검증 성공", content = {
            @Content(schema = @Schema(implementation = Boolean.class))
    })
    @GetMapping("/api/moims/{moimId}/creator")
    public ResponseEntity<?> isCreator(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) {
        try {
            String username = customOAuth2Member.getUsername();
            return ResponseEntity.ok().body(meetingService.isCreator(username, moimId));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "모임 참여자 여부 검증")
    @ApiResponse(responseCode = "200", description = "모임 참여자 여부 검증 성공", content = {
            @Content(schema = @Schema(implementation = Boolean.class))
    })
    @GetMapping("/api/moims/{moimId}/participant")
    public ResponseEntity<?> isParticipant(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) {
        try {
            String username = customOAuth2Member.getUsername();
            Member member = memberService.findMember(username);
            return ResponseEntity.ok().body(meetingService.isParticipant(moimId, member));
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "모임 임베딩 전체 강제 갱신")
    @ApiResponse(responseCode = "200", description = "모임 임베딩 전체 강제 갱신 요청 성공", content = {
            @Content(schema = @Schema(implementation = String.class))
    })
    @GetMapping("/admin/moims/embedding")
    public ResponseEntity<String> updateMoimEmbeddingAll(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            Member member = memberService.findMember(customOAuth2Member.getUsername());

            if (member.getRole().equals("ROLE_ADMIN")) {
                meetingService.updateMoimEmbeddingAll();
                return ResponseEntity.ok( "Request Sent");
            }
            else {
                return ResponseEntity.badRequest().build();
            }
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
