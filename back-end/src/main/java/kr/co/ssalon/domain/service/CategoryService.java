package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.Category;
import kr.co.ssalon.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findCategory(Long id) throws BadRequestException {
        Optional<Category> findCategory = categoryRepository.findById(id);
        Category category = validationCategory(findCategory);
        return category;
    }

    private Category validationCategory(Optional<Category> category) throws BadRequestException {
        if (category.isPresent()) {
            return category.get();
        }else
            throw new BadRequestException("해당 회원을 찾을 수 없습니다");
    }

}
