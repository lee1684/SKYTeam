package kr.co.ssalon.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlackListSearchPageDTO<T>{
    private List<T> content;
    private Boolean hasNext;

    public BlackListSearchPageDTO(Page<T> reports) {
        this.content = reports.getContent();
        this.hasNext = reports.hasNext();
    }
}
