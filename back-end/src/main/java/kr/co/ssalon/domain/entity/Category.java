package kr.co.ssalon.domain.entity;


import jakarta.persistence.*;
import kr.co.ssalon.web.dto.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;
    private String description;
    private String imageUrl;

    protected Category() {
    }

    public void changeName(String name) { this.name = name; }
    public void changeDescription(String description) { this.description = description; }
    public void changeImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public static Category createCategory(String name, String description, String imageUrl) {
        Category category = Category
                .builder()
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .build();
        return category;
    }

    public static Category createCategory(CategoryDTO categoryDTO) {
        Category category = Category
                .builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .imageUrl(categoryDTO.getImageUrl())
                .build();

        return category;
    }

    public void updateCategory(CategoryDTO categoryDTO) {
        changeName(categoryDTO.getName());
        changeDescription(categoryDTO.getDescription());
        changeImageUrl(categoryDTO.getImageUrl());
    }
}