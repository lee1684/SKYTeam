package kr.co.ssalon.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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

    private String title;
    private String image_url;
    private String description;


    protected Diary() {
    }

    // ***** 연관 메서드 *****
    public void settingMemberMeeting(MemberMeeting memberMeeting) {
        this.memberMeeting = memberMeeting;
    }

    public static Diary createDiary(String title, String image_url, String description) {
        Diary diary = Diary.builder()
                .title(title)
                .image_url(image_url)
                .description(description)
                .build();
        return diary;
    }

}
