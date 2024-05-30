package kr.co.ssalon.web.controller;

import kr.co.ssalon.domain.service.AttendanceService;
import kr.co.ssalon.web.controller.annotation.WithCustomMockUser;
import kr.co.ssalon.web.dto.AttendanceDTO;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@WebMvcTest(AttendanceController.class)
public class AttendanceControllerTest {

    @MockBean
    private AttendanceService attendanceService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AttendanceController attendanceController;

    @Test
    @DisplayName("모임 출석자 목록 조회 API(GET /api/moims/{moimId}/attendance) 테스트")
    @WithCustomMockUser(username = "test")
    public void 모임출석자목록조회API() throws Exception {
        // given

        Long moimId = 1L;

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/moims/"+moimId+"/attendance").with(csrf()));

        // then
        resultActions.andExpect(status().isOk());

    }

    @Test
    @DisplayName("출석 상태 변경 API(POST /api/moims/{moimId}/attendance/{userId}/{attendance}) 테스트")
    @WithCustomMockUser(username = "test")
    public void 출석상태변경API() throws Exception {
        // given
        Long moimId = 1L;
        Long userId = 2L;
        Boolean attendance = true;

        when(attendanceService.changeAttendance(moimId, userId, attendance)).thenReturn(attendance);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/moims/"+moimId+"/attendance/"+userId+"/"+attendance).with(csrf()));

        // then
        resultActions.andExpect(status().isOk());
    }

}