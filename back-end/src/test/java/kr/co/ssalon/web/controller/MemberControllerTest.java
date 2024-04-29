package kr.co.ssalon.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberDates;
import kr.co.ssalon.domain.entity.Region;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.controller.annotation.WithCustomMockUser;
import kr.co.ssalon.web.dto.MemberDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class MemberControllerTest {

    @MockBean
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 API(POST /api/auth/signup) 테스트")
    @WithCustomMockUser()
    public void 회원가입API() throws Exception {
        // given

        // 현재 소셜 로그인한 유저(@WithCustomMockUser)의 username 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2Member customOAuth2Member = (CustomOAuth2Member) authentication.getPrincipal();
        String username = customOAuth2Member.getUsername();

        // 온보딩 페이지에서 입력된 추가정보
        MemberDTO additionalInfo = MemberDTO.builder()
                .nickname("닉네임")
                .interests(new ArrayList<>(Arrays.asList("독서", "영화감상")))
                .address(Region.GANGWONDO.getLocalName())
                .build();

        // MemberService.signup() stub
        MemberDates memberDates = new MemberDates();
        memberDates.prePersist();
        Member joinedMember = Member.builder()
                        .nickname("닉네임")
                        .interests(new ArrayList<>(Arrays.asList("독서", "영화감상")))
                        .address(Region.GANGWONDO.getLocalName())
                        .memberDates(memberDates)
                        .build();
        Mockito.when(memberService.signup(username, additionalInfo)).thenReturn(joinedMember);

        // when
        String requestBody = objectMapper.writeValueAsString(additionalInfo);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname", is("닉네임")))
                .andExpect(jsonPath("$.interests", contains("독서","영화감상")))
                .andExpect(jsonPath("$.address", is(Region.GANGWONDO.getLocalName())));
    }
}
