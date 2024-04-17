package kr.co.ssalon.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;
    private String description;
    private String image_url;

    protected Category() {}

    public static Category create_category(){
        Category category = new Category();
        return category;
    }

}
