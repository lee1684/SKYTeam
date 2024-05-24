package kr.co.ssalon.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.config.WebSocketConfig;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.service.ChatService;
import kr.co.ssalon.domain.service.MeetingService;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Tag(name = "채팅")
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final MemberService memberService;
    private final MeetingService meetingService;
    private final WebSocketConfig webSocketConfig;

    @MessageMapping("/{roomId}")
    @SendTo("/room/{roomId}")
    public MessageDTO chat(SimpMessageHeaderAccessor accessor, @DestinationVariable Long roomId, @Payload Map<String, String> message) throws BadRequestException {
        String username = (String) accessor.getSessionAttributes().get("username");
        Member member = memberService.findMember(username);
        Meeting meeting = meetingService.findMeeting(roomId);

        // 채팅 메시지 저장
        MessageDTO messageDTO = chatService.saveMessage(member, meeting, message.get("message"));

        return messageDTO;
    }

    @MessageMapping("/disconnect")
    public ResponseEntity<?> disconnect() {
        return ResponseEntity.ok("disconnect");
    }

    @Operation(summary = "특정 모임 채팅 조회")
    @ApiResponse(responseCode = "200", description = "특정 모임 채팅 조회 성공", content = {
            @Content(schema = @Schema(implementation = MessageDTO.class))
    })
    @GetMapping("/api/chat/{moimId}")
    public ResponseEntity<?> getMoimChat(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) throws BadRequestException {
        String username = customOAuth2Member.getUsername();
        Member currentUser = memberService.findMember(username);

        if (!meetingService.isParticipant(moimId, currentUser)) {
            return new ResponseEntity<>("회원이 참여한 모임이 아닙니다.", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().body(chatService.getChatList(moimId));
    }

    @Operation(summary = "특정 모임 실시간 채팅 참여자 조회")
    @ApiResponse(responseCode = "200", description = "특정 모임 실시간 채팅 참여자 조회 성공", content = {
            @Content(schema = @Schema(implementation = ChatConnectedMemberDTO.class))
    })
    @GetMapping("/api/chat/{moimId}/users")
    public ResponseEntity<List<ChatConnectedMemberDTO>> getConnectedUser(@PathVariable Long moimId) {
        List<ChatConnectedMemberDTO> chatConnectedUserList = new ArrayList<>();
        webSocketConfig.getConnectedMembers().forEach((key, value) -> {
            if (Long.valueOf(value).equals(moimId)) {
                try {
                    Member connectedMember = memberService.findMember(key);
                    String nickname = connectedMember.getNickname();
                    String profilePictureUrl = connectedMember.getProfilePictureUrl();
                    ChatConnectedMemberDTO chatConnectedMemberDTO = new ChatConnectedMemberDTO(nickname, profilePictureUrl);
                    chatConnectedUserList.add(chatConnectedMemberDTO);
                } catch (BadRequestException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return ResponseEntity.ok().body(chatConnectedUserList);
    }
}
