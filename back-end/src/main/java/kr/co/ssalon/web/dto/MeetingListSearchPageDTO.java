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
public class MeetingListSearchPageDTO <T>{
    private List<T> content;
    private Boolean hasNext;

    public MeetingListSearchPageDTO(Page<T> moims) {
        this.content = moims.getContent();
        this.hasNext = moims.hasNext();
    }

    public MeetingListSearchPageDTO(List<T> content, Boolean hasNext) {
        this.content = content;
        this.hasNext = hasNext;
    }
}
