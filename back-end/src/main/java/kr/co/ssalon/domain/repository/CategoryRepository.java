package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    Optional<Category> findByName(String categoryName);
}
