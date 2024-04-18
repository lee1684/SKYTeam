package kr.co.ssalon.ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.ssalon.Controller.TicketConroller;
import kr.co.ssalon.domain.entity.Ticket;
import kr.co.ssalon.dto.DiaryDTO;
import kr.co.ssalon.dto.TicketDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static kr.co.ssalon.domain.entity.Ticket.create_ticket;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketConroller.class)
@AutoConfigureMockMvc
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("증표 앞면 조회 API (GET:/tickets/{moimId}) 테스트")
    public void getTicketFrontById() throws Exception {
        // Given

        // When - Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tickets/{moimId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.moimId").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.meeting").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.decoration").exists())
                ;
    }

    @Test
    @DisplayName("증표 앞면 등록 API (POST:/tickets/{moimId}) 테스트")
    public void createTicketFrontById() throws Exception {
        // Given
        TicketDTO ticketDTO = TicketDTO.builder()
                .decoration("fwf9w8df92h098e2g28egtest")
                .build();

        // When - Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/tickets/{moimId}")
                        .content(objectMapper.writeValueAsString(ticketDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.decoration").value("fwf9w8df92h098e2g28egtest"))
                ;
    }

    @Test
    @DisplayName("증표 앞면 수정 API (PUT:/tickets/{moimId}) 테스트")
    public void updateTicketFrontById() throws Exception {
        // Given
        TicketDTO ticketDTO = TicketDTO.builder()
                .decoration("fwf9w8df92h098e2g28egtest")
                .build();

        // When - Then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/tickets/{moimId}")
                        .content(objectMapper.writeValueAsString(ticketDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                ;
    }

    @Test
    @DisplayName("증표 뒷면 조회 API (GET:/tickets/{moimId}/{userId}) 테스트")
    public void getTicketBackById() throws Exception {
        // Given

        // When - Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/tickets/{moimId}/{userId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.image_url").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.decoration").exists())
                ;
    }

    @Test
    @DisplayName("증표 뒷면 등록 API (POST:/tickets/{moimId}/{userId}) 테스트")
    public void createTicketBackById() throws Exception {
        // Given
        DiaryDTO diaryDTO = DiaryDTO.builder()
                .id(1)
                .title("Diary Test Title")
                .image_url("http://test")
                .description("Diary Test Description")
                .decoration("fwf9w8df92h098e2g28egtest")
                .build();

        // When - Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/tickets/{moimId}/{userId}")
                        .content(objectMapper.writeValueAsString(diaryDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Diary Test Title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.image_url").value("http://test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Diary Test Description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.decoration").value("fwf9w8df92h098e2g28egtest"))
                ;
    }

    @Test
    @DisplayName("증표 뒷면 수정 API (PUT:/tickets/{moimId}/{userId}) 테스트")
    public void updateTicketBackById() throws Exception {
        // Given
        DiaryDTO diaryDTO = DiaryDTO.builder()
                .id(1)
                .title("Diary Test Title")
                .image_url("http://test")
                .description("Diary Test Description")
                .decoration("fwf9w8df92h098e2g28egtest")
                .build();

        // When - Then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/tickets/{moimId}/{userId}")
                        .content(objectMapper.writeValueAsString(diaryDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                ;
    }
//
//    @Test
//    @DisplayName("사용자 보유 증표 목록 조회 API (GET:/users/{userId}/tickets) 테스트")
//    public void getTicketsById() throws Exception {
//        // Given
//
//        // When - Then
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/users/{userId}/tickets", 1)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.tickets").exists())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.tickets[*].ticketId").isNotEmpty())
//                ;
//    }
//
//    @Test
//    @DisplayName("증표 공유 링크 생성/조회 API (GET:/tickets/{moimId}/link) 테스트")
//    public void getLinkById() throws Exception {
//        // Given
//
//        // When - Then
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/tickets/{moimId}/link", 1)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.moimId").value(1))
//        ;
//    }
//
//    @Test
//    @DisplayName("증표 공유 링크 삭제 API (DELETE:/tickets/{moimId}/link) 테스트")
//    public void deleteLinkById() throws Exception {
//        // Given
//
//        // When - Then
//        mockMvc.perform(MockMvcRequestBuilders
//                        .delete("/tickets/{moimId}/link", 1))
//                .andDo(print())
//                .andExpect(status().isAccepted())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.moimId").value(1))
//        ;
//    }
}
