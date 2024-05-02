package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.Category;
import kr.co.ssalon.domain.repository.CategoryRepository;
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


}
