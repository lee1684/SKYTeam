package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private Member creator;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @OneToMany(mappedBy = "meeting")
    private final List<MemberMeeting> participants = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "meeting_picture", joinColumns = @JoinColumn(name = "meeting_id"))
    private final List<String> meetingPictureUrls = new ArrayList<>();


    private String title;
    private String description;
    private String location;
    private Integer capacity;
    private LocalDateTime meetingDate;

    protected Meeting() {}

    public static Meeting createMeeting() {
        // Change to builder
        Meeting meeting = Meeting.builder().build();
        return meeting;
    }

}
