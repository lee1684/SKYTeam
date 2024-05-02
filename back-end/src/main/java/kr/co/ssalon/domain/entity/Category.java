package kr.co.ssalon.domain.entity;


import jakarta.persistence.*;
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

}
