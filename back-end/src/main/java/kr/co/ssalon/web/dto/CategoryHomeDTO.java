package kr.co.ssalon.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kr.co.ssalon.domain.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryHomeDTO {
    @NotNull
    private Long id;
    @NotBlank
    private String name;

    public CategoryHomeDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
    }

}
