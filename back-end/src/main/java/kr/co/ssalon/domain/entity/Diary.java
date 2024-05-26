package kr.co.ssalon.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private MemberMeeting memberMeeting;

    @Builder.Default
    private boolean editYet = true;

    private String title;

    @ElementCollection
    @CollectionTable(name = "diary_picture", joinColumns = @JoinColumn(name = "diary_id"))
    private final List<String> diaryPictureUrls = new ArrayList<>();

    private String description;


    protected Diary() {
    }

    public void addDiaryPictureUrls(List<String> diaryPictureUrls) {
        for (String diaryPictureUrl : diaryPictureUrls) {
            getDiaryPictureUrls().add(diaryPictureUrl);
        }
        this.editYet = false;
    }

    public void editDiaryDescription(String description) {
        this.description = description;
        this.editYet = false;
    }

    // ***** 연관 메서드 *****
    public void settingMemberMeeting(MemberMeeting memberMeeting) {
        this.memberMeeting = memberMeeting;
    }

    public static Diary createDiary(String title, List<String> diaryPictureUrls, String description) {
        Diary diary = Diary.builder()
                .title(title)
                .description(description)
                .editYet(true)
                .build();
        diary.addDiaryPictureUrls(diaryPictureUrls);

        return diary;
    }

}
