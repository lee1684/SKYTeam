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


    protected Diary() {}

    public static Diary createDiary() {
        Diary diary = Diary.builder().build();
        return diary;
    }

}
