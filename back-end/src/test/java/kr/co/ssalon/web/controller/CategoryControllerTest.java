package kr.co.ssalon.web.controller;

import kr.co.ssalon.domain.entity.Category;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.repository.CategoryRepository;
import kr.co.ssalon.domain.service.CategoryService;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.web.controller.annotation.WithCustomMockUser;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("카테고리 전체 조회 API(GET /api/category/all) 테스트")
    @WithCustomMockUser(username = "test")
    public void 카테고리전체조회API() throws Exception {
        // given

        List<Category> categories = new ArrayList<>();

        Category category1 = Category.builder()
                .id(1L)
                .name("categoryNameTest1")
                .description("categoryDescriptionTest1")
                .imageUrl("categoryImageUrlTest1")
                .build();

        Category category2 = Category.builder()
                .id(2L)
                .name("categoryNameTest2")
                .description("categoryDescriptionTest2")
                .imageUrl("categoryImageUrlTest2")
                .build();

        categories.add(category1);
        categories.add(category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/category/all").with(csrf()));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("categoryNameTest1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("categoryNameTest2")))
        ;
    }

    @Test
    @DisplayName("추천 카테고리 리스트 전체 조회 API(GET /api/category/recommend) 테스트")
    @WithCustomMockUser(username = "test")
    public void 추천카테고리전체조회API() throws Exception {
        // given

        String username = "test";

        String categoryRecommendation = "[2,1]";

        Member member = Member.builder()
                .categoryRecommendation(categoryRecommendation)
                .build();

        List<Category> categories = new ArrayList<>();

        Category category1 = Category.builder()
                .id(1L)
                .name("categoryNameTest1")
                .description("categoryDescriptionTest1")
                .imageUrl("categoryImageUrlTest1")
                .build();

        Category category2 = Category.builder()
                .id(2L)
                .name("categoryNameTest2")
                .description("categoryDescriptionTest2")
                .imageUrl("categoryImageUrlTest2")
                .build();

        categories.add(category1);
        categories.add(category2);

        when(categoryService.findCategory(1L)).thenReturn(category1);
        when(categoryService.findCategory(2L)).thenReturn(category2);
        when(memberService.findMember(username)).thenReturn(member);
        when(categoryRepository.findAll()).thenReturn(categories);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/category/recommend").with(csrf()));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].name", is("categoryNameTest2")))
                .andExpect(jsonPath("$[1].id", is(1)))
                .andExpect(jsonPath("$[1].name", is("categoryNameTest1")))
        ;
    }
}
