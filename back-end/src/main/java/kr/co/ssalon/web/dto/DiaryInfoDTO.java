package kr.co.ssalon.web.dto;

import kr.co.ssalon.domain.entity.Diary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class DiaryInfoDTO {

    private String description;
    @Builder.Default
    private List<String> diaryPictureUrls = new ArrayList<>();

    public DiaryInfoDTO(String description, List<String> diaryPictureUrls) {
        this.description = description;
        this.setDiaryPictureUrls(diaryPictureUrls);
    }

    public DiaryInfoDTO(Diary diary) {
        this.setDescription(diary.getDescription());
        this.setDiaryPictureUrls(diary.getDiaryPictureUrls().isEmpty() ? List.of("") : diary.getDiaryPictureUrls());
    }

    public void setDiaryPictureUrls(List<String> diaryPictureUrls) {
        for (String diaryPictureUrl : diaryPictureUrls) {
            getDiaryPictureUrls().add(diaryPictureUrl);
        }
    }
}
