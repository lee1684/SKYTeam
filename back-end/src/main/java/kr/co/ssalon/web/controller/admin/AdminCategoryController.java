package kr.co.ssalon.web.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.ssalon.domain.entity.Category;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.repository.CategoryRepository;
import kr.co.ssalon.domain.service.CategoryService;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.domain.service.ValidationService;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자 카테고리")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final ValidationService validationService;
    private final MemberService memberService;

    @Operation(summary = "카테고리 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 생성 성공"),
    })
    @PostMapping("/api/admin/category")
    public ResponseEntity<?> createCategory(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @RequestBody CategoryDTO categoryDTO) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);
            return ResponseEntity.ok().body(categoryRepository.save(Category.createCategory(categoryDTO)));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "카테고리 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 전체 조회 성공"),
    })
    @GetMapping("/api/admin/category")
    public ResponseEntity<?> getCategories(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);
            return ResponseEntity.ok().body(categoryRepository.findAll());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "카테고리 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 조회 성공"),
    })
    @GetMapping("/api/admin/category/{categoryId}")
    public ResponseEntity<?> getCategory(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long categoryId) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);
            return ResponseEntity.ok().body(categoryService.findCategory(categoryId));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "카테고리 내용 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "카테고리 내용 수정 성공"),
    })
    @PatchMapping("/api/admin/category/{categoryId}")
    public ResponseEntity<?> editCategory(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long categoryId, @RequestBody CategoryDTO categoryDTO) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);
            return ResponseEntity.ok().body(categoryService.editCategory(categoryId, categoryDTO));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*
    @Operation(summary = "특정 카테고리 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 카테고리 삭제 성공"),
    })
    @DeleteMapping("/api/admin/category/{categoryId}")
    public ResponseEntity<?> deleteCategory(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable Long categoryId) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);
            categoryRepository.deleteById(categoryId);
            return ResponseEntity.ok().body(categoryId);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(summary = "모든 카테고리 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 카테고리 삭제 성공"),
    })
    @DeleteMapping("/api/admin/category")
    public ResponseEntity<?> deleteCategories(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member) {
        try {
            String username = customOAuth2Member.getUsername();
            validationAdmin(username);
            categoryRepository.deleteAll();
            return ResponseEntity.ok().body("delete success");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    */

    private void validationAdmin(String username) throws BadRequestException {
        Member member = memberService.findMember(username);
        validationService.validationAdmin(member.getRole());
    }
}