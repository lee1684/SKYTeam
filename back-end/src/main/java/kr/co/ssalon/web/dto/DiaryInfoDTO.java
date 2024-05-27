package kr.co.ssalon.web.dto;

import kr.co.ssalon.domain.entity.Diary;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaryInfoDTO {

    private String description;
    @Builder.Default
    private List<String> diaryPictureUrls = new ArrayList<>();

    public DiaryInfoDTO(Diary diary) {
        this.setDescription(diary.getDescription());
        this.setDiaryPictureUrls(diary.getDiaryPictureUrls().isEmpty() ? List.of("") : diary.getDiaryPictureUrls());
    }

//    public void setDiaryPictureUrls(List<String> diaryPictureUrls) {
//        if (diaryPictureUrls.isEmpty()) {
//            return;
//        }
//        for (String diaryPictureUrl : diaryPictureUrls) {
//            getDiaryPictureUrls().add(diaryPictureUrl);
//        }
//    }
}
