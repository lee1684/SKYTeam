package kr.co.ssalon.web.dto;

import kr.co.ssalon.domain.entity.Diary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class DiaryInfoDTO {

    private String description;
    @Builder.Default
    private List<String> diaryPictureUrls = new ArrayList<>();

    public DiaryInfoDTO() {
        this.description = "";
        this.diaryPictureUrls = new ArrayList<>();
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
