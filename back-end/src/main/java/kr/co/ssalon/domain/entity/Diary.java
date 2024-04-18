package kr.co.ssalon.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
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

    public static Diary create_diary() {
        Diary diary = new Diary();
        return diary;
    }

}
