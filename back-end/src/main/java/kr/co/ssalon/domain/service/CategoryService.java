package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.dto.MeetingDomainDTO;
import kr.co.ssalon.domain.entity.Category;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.repository.CategoryRepository;
import kr.co.ssalon.web.dto.CategoryDTO;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findCategory(Long id) throws BadRequestException {
        Optional<Category> findCategory = categoryRepository.findById(id);
        Category category = ValidationService.validationCategory(findCategory);
        return category;
    }

    @Transactional
    public Category editCategory(Long categoryId, CategoryDTO categoryDTO) throws BadRequestException {
        Category category = findCategory(categoryId);
        category.updateCategory(categoryDTO);
        return category;
    }

    public void isCategoryNameExists(String categoryName) throws BadRequestException {
        Optional<Category> existingCategory = categoryRepository.findByName(categoryName);
        if (existingCategory.isPresent()) {
            throw new BadRequestException("이미 존재하는 카테고리 이름입니다: " + categoryName);
        }
    }
}
