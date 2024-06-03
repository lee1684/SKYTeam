package kr.co.ssalon.web.controller;

import kr.co.ssalon.config.WebSocketConfig;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.Message;
import kr.co.ssalon.domain.service.ChatService;
import kr.co.ssalon.domain.service.MeetingService;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.web.controller.annotation.WithCustomMockUser;
import kr.co.ssalon.web.dto.ChatConnectedMemberDTO;
import kr.co.ssalon.web.dto.MessageResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.*;


import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@WebMvcTest(ChatController.class)
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;
    @MockBean
    private MemberService memberService;
    @MockBean
    private  MeetingService meetingService;
    @MockBean
    private WebSocketConfig webSocketConfig;

    /* WebSocket 관련 테스트 코드 추가 필요 */

    @Test
    @DisplayName("특정 모임 채팅 기록 조회 API(GET /api/chat-history/{moimId}) 테스트")
    @WithCustomMockUser(username = "test")
    public void 특정모임채팅기록조회API() throws Exception {
        // given
        Long moimId = 1L;
        String username = "test";
        List<MessageResponseDTO> chatHistory = new ArrayList<>();
        Member currentUser = Member.builder()
                .username(username)
                .role("ROLE_USER")
                .build();
        // 가정한 채팅 기록을 생성하여 리스트에 추가
        MessageResponseDTO messageResponseDTO1 = MessageResponseDTO.builder()
                .messageType("messageTypeTest1")
                .nickname("nicknameTest1")
                .profilePicture("profilePictureTest1")
                .message("messageTest1")
                .date(LocalDateTime.now())
                .email("emailTest1")
                .imageUrl("imageUrl1")
                .build();
        MessageResponseDTO messageResponseDTO2 = MessageResponseDTO.builder()
                .messageType("messageTypeTest2")
                .nickname("nicknameTest2")
                .profilePicture("profilePictureTest2")
                .message("messageTest2")
                .date(LocalDateTime.now())
                .email("emailTest2")
                .imageUrl("imageUrl2")
                .build();
        chatHistory.add(messageResponseDTO1);
        chatHistory.add(messageResponseDTO2);

        // chatService.getMoimChatHistory(moimId)가 호출될 때, 가정한 chatHistory를 리턴하도록 설정
        when(memberService.findMember(username)).thenReturn(currentUser);
        when(meetingService.isParticipant(moimId, currentUser)).thenReturn(true);
        when(chatService.getMoimChatHistory(moimId)).thenReturn(chatHistory);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/chat-history/"+ moimId)
                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].messageType", is("messageTypeTest1")))
                .andExpect(jsonPath("$[0].nickname", is("nicknameTest1")))
                .andExpect(jsonPath("$[0].profilePicture", is("profilePictureTest1")))
                .andExpect(jsonPath("$[0].message", is("messageTest1")))
                .andExpect(jsonPath("$[0].date").exists()) // 날짜 필드가 존재하는지 확인
                .andExpect(jsonPath("$[0].email", is("emailTest1")))
                .andExpect(jsonPath("$[0].imageUrl", is("imageUrl1")))
                .andExpect(jsonPath("$[1].messageType", is("messageTypeTest2")))
                .andExpect(jsonPath("$[1].nickname", is("nicknameTest2")))
                .andExpect(jsonPath("$[1].profilePicture", is("profilePictureTest2")))
                .andExpect(jsonPath("$[1].message", is("messageTest2")))
                .andExpect(jsonPath("$[1].date").exists())
                .andExpect(jsonPath("$[1].email", is("emailTest2")))
                .andExpect(jsonPath("$[1].imageUrl", is("imageUrl2")))
        ;
    }

    @Test
    @DisplayName("회원 채팅 기록 조회 API(GET /api/chat-history/me) 테스트")
    @WithCustomMockUser(username = "test")
    public void 회원채팅기록조회API() throws Exception {
        // given
        String username = "test";
        Long userId = 1L;
        Member currentUser = Member.builder()
                .id(userId)
                .username(username)
                .build();
        List<MessageResponseDTO> chatHistory = new ArrayList<>();
        // 가정한 채팅 기록을 생성하여 리스트에 추가
        MessageResponseDTO messageResponseDTO1 = MessageResponseDTO.builder()
                .messageType("messageTypeTest1")
                .nickname("nicknameTest1")
                .profilePicture("profilePictureTest1")
                .message("messageTest1")
                .date(LocalDateTime.now())
                .email("emailTest1")
                .imageUrl("imageUrl1")
                .build();
        MessageResponseDTO messageResponseDTO2 = MessageResponseDTO.builder()
                .messageType("messageTypeTest2")
                .nickname("nicknameTest2")
                .profilePicture("profilePictureTest2")
                .message("messageTest2")
                .date(LocalDateTime.now())
                .email("emailTest2")
                .imageUrl("imageUrl2")
                .build();
        chatHistory.add(messageResponseDTO1);
        chatHistory.add(messageResponseDTO2);

        // memberService.findMember(username) 호출 시 currentUser를 리턴하도록 설정
        when(memberService.findMember(username)).thenReturn(currentUser);
        // chatService.getMyChatHistory(userId) 호출 시 가정한 chatHistory를 리턴하도록 설정
        when(chatService.getMyChatHistory(userId)).thenReturn(chatHistory);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/chat-history/me")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].messageType", is("messageTypeTest1")))
                .andExpect(jsonPath("$[0].nickname", is("nicknameTest1")))
                .andExpect(jsonPath("$[0].profilePicture", is("profilePictureTest1")))
                .andExpect(jsonPath("$[0].message", is("messageTest1")))
                .andExpect(jsonPath("$[0].date").exists()) // 날짜 필드가 존재하는지 확인
                .andExpect(jsonPath("$[0].email", is("emailTest1")))
                .andExpect(jsonPath("$[0].imageUrl", is("imageUrl1")))
                .andExpect(jsonPath("$[1].messageType", is("messageTypeTest2")))
                .andExpect(jsonPath("$[1].nickname", is("nicknameTest2")))
                .andExpect(jsonPath("$[1].profilePicture", is("profilePictureTest2")))
                .andExpect(jsonPath("$[1].message", is("messageTest2")))
                .andExpect(jsonPath("$[1].date").exists())
                .andExpect(jsonPath("$[1].email", is("emailTest2")))
                .andExpect(jsonPath("$[1].imageUrl", is("imageUrl2")))
        ;

    }

    @Test
    @DisplayName("특정 모임 채팅 실시간 참가 회원 조회 API(GET /api/chat/{moimId}/users) 테스트")
    @WithCustomMockUser(username = "test")
    public void 특정모임채팅실시간참가회원조회API() throws Exception {
        // given
        Long moimId = 1L;

        // 가정한 현재 연결된 회원 리스트
        Map<String, String> connectedMembers = new HashMap<>();
        connectedMembers.put("user1", String.valueOf(moimId));
        connectedMembers.put("user2", String.valueOf(moimId));
        connectedMembers.put("user3", String.valueOf(2L)); // 다른 모임에 연결된 사용자도 추가
        when(webSocketConfig.getConnectedMembers()).thenReturn(connectedMembers);

        Member connectedMember1 = Member.builder()
                .nickname("User 1")
                .profilePictureUrl("profile_picture_url_1")
                .build();
        Member connectedMember2 = Member.builder()
                .nickname("User 2")
                .profilePictureUrl("profile_picture_url_2")
                .build();
        when(memberService.findMember("user1")).thenReturn(connectedMember1);
        when(memberService.findMember("user2")).thenReturn(connectedMember2);

        List<ChatConnectedMemberDTO> expectedConnectedUsers = Arrays.asList(
                new ChatConnectedMemberDTO("User 1", "profile_picture_url_1"),
                new ChatConnectedMemberDTO("User 2", "profile_picture_url_2")
        );

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/chat/" + moimId + "/users")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nickname", is("User 1")))
                .andExpect(jsonPath("$[0].profilePictureUrl", is("profile_picture_url_1")))
                .andExpect(jsonPath("$[1].nickname", is("User 2")))
                .andExpect(jsonPath("$[1].profilePictureUrl", is("profile_picture_url_2")));

    }

}
