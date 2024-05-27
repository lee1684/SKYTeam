package kr.co.ssalon.web.dto;

import kr.co.ssalon.domain.entity.Diary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiaryInfoDTO {

    private String description;
    @Builder.Default
    private List<String> diaryPictureUrls = new ArrayList<>();

    public DiaryInfoDTO(Diary diary) {
        this.setDescription(diary.getDescription());
        this.addDiaryPictureUrls(diary.getDiaryPictureUrls());
    }

    public void addDiaryPictureUrls(List<String> diaryPictureUrls) {
        for (String diaryPictureUrl : diaryPictureUrls) {
            getDiaryPictureUrls().add(diaryPictureUrl);
        }
    }
}
