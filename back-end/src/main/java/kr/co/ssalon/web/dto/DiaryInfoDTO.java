package kr.co.ssalon.web.dto;

import kr.co.ssalon.domain.entity.Diary;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
public class DiaryInfoDTO {

    private String description;
    @Builder.Default
    private List<String> diaryPictureUrls = new ArrayList<>();

    public DiaryInfoDTO(String description, List<String> diaryPictureUrls) {
        this.description = description;
        this.setDiaryPictureUrls(diaryPictureUrls.isEmpty() ? List.of("") : diaryPictureUrls);
    }

    public DiaryInfoDTO(Diary diary) {
        this.setDescription(diary.getDescription());
        this.setDiaryPictureUrls(diary.getDiaryPictureUrls().isEmpty() ? List.of("") : diary.getDiaryPictureUrls());
    }

    public void setDiaryPictureUrls(List<String> diaryPictureUrls) {
        if (diaryPictureUrls.isEmpty()) {
            return;
        }
        for (String diaryPictureUrl : diaryPictureUrls) {
            getDiaryPictureUrls().add(diaryPictureUrl);
        }
    }
}
