package kr.co.ssalon.domain.entity;


import jakarta.persistence.*;
import kr.co.ssalon.web.dto.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
}
