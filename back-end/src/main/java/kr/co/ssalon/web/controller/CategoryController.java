package kr.co.ssalon.web.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.domain.entity.Category;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.repository.CategoryRepository;
import kr.co.ssalon.domain.service.CategoryService;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.CategoryHomeDTO;
import kr.co.ssalon.web.dto.JsonResult;
import kr.co.ssalon.web.dto.MeetingListSearchDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "카테고리")
@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final MemberService memberService;

    @Operation(summary = "카테고리 전체 조회")
    @ApiResponse(responseCode = "200", description = "카테고리 전체 조회 성공", content = {
            @Content(schema = @Schema(implementation = Category.class))
    })
    @GetMapping("/api/category/all")
    public ResponseEntity<?> getCategories(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            List<Category> categories = categoryRepository.findAll();
            List<CategoryHomeDTO> categoryHomeDTOS = new ArrayList<>();

            for (Category category : categories) {
                CategoryHomeDTO categoryHomeDTO = new CategoryHomeDTO(category);
                categoryHomeDTOS.add(categoryHomeDTO);
            }

            return ResponseEntity.ok().body(categoryHomeDTOS);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "추천 카테고리 리스트 전체 조회")
    @ApiResponse(responseCode = "200", description = "카테고리 전체 조회 성공", content = {
            @Content(schema = @Schema(implementation = CategoryHomeDTO.class))
    })
    @GetMapping("/api/category/recommend")
    public ResponseEntity<?> getRecommendCategories(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            Gson gson = new Gson();

            String username = customOAuth2Member.getUsername();
            Member member = memberService.findMember(username);

            List<Long> categoryRecommendList = gson.fromJson(member.getCategoryRecommendation(), new TypeToken<List<Long>>() {});

            List<CategoryHomeDTO> categoryHomeDTOS = new ArrayList<>();

            for (int i = 0; i < categoryRecommendList.size(); i++) {
                Category category;

                if (member.getCategoryRecommendation() != null) {
                    // member.getMeetingRecommendation()이 null이 아닌 경우
                    category = categoryService.findCategory(categoryRecommendList.get(i));
                } else {
                    // member.getMeetingRecommendation()이 null인 경우
                    category = categoryService.findCategory((long) i);
                }

                CategoryHomeDTO categoryHomeDTO = new CategoryHomeDTO(category);
                categoryHomeDTOS.add(categoryHomeDTO);
            }
            return ResponseEntity.ok().body(new JsonResult<>(categoryHomeDTOS).getData());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }




}
