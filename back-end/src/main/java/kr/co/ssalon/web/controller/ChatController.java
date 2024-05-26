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
import java.util.Objects;

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
    public MessageResponseDTO chat(SimpMessageHeaderAccessor accessor, @DestinationVariable Long roomId, @Payload MessageRequestDTO messageRequestDTO) throws BadRequestException {
        String username = (String) accessor.getSessionAttributes().get("username");
        String messageType = (String) accessor.getSessionAttributes().get("messageType");

        Member member = memberService.findMember(username);
        Meeting meeting = meetingService.findMeeting(roomId);

        String imageUrl = Objects.equals(messageType, "ENTER")
                ? ""
                : chatService.changeImageBytesToUrl(messageRequestDTO.getImageBytes(), messageRequestDTO.getFileName(), roomId);

        // 채팅 메시지 저장
        MessageResponseDTO messageResponseDTO = chatService.saveMessage(member, meeting, messageRequestDTO.getMessage(), messageType, imageUrl);

        return messageResponseDTO;
    }

    @MessageMapping("/disconnect")
    public ResponseEntity<?> disconnect() {
        return ResponseEntity.ok("disconnect");
    }

    @Operation(summary = "특정 모임 채팅 기록 조회")
    @ApiResponse(responseCode = "200", description = "특정 모임 채팅 기록 조회 성공", content = {
            @Content(schema = @Schema(implementation = MessageResponseDTO.class))
    })
    @GetMapping("/api/chat-history/{moimId}")
    public ResponseEntity<?> getMoimChatHistory(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long moimId) throws BadRequestException {
        String username = customOAuth2Member.getUsername();
        Member currentUser = memberService.findMember(username);

        if (currentUser.getRole() == "ROLE_USER" && !meetingService.isParticipant(moimId, currentUser)) {
            return new ResponseEntity<>("회원이 참여한 모임이 아닙니다.", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().body(chatService.getMoimChatHistory(moimId));
    }

    @Operation(summary = "회원 채팅 기록 조회")
    @ApiResponse(responseCode = "200", description = "회원 채팅 기록 조회 성공", content = {
            @Content(schema = @Schema(implementation = MessageResponseDTO.class))
    })
    @GetMapping("/api/chat-history/me")
    public ResponseEntity<?> getMyChatHistory(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) throws BadRequestException {
        String username = customOAuth2Member.getUsername();
        Member currentUser = memberService.findMember(username);

        return ResponseEntity.ok().body(chatService.getMyChatHistory(currentUser.getId()));
    }

    @Operation(summary = "특정 모임 채팅 실시간 참가 회원 조회")
    @ApiResponse(responseCode = "200", description = "특정 모임 채팅 실시간 참가 회원 조회 성공", content = {
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
