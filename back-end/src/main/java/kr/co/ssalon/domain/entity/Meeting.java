package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.apache.coyote.BadRequestException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private Member creator;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MemberMeeting> participants = new ArrayList<>();

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MeetingOut> meetingOuts = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "meeting_picture", joinColumns = @JoinColumn(name = "meeting_id"))
    private final List<String> meetingPictureUrls = new ArrayList<>();

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Payment> payments = new ArrayList<>();

    private String title;
    private String description;
    private String location;
    private Integer capacity;
    private Integer payment;
    private LocalDateTime meetingDate;

    private Boolean isSharable;
    @Builder.Default
    private Boolean isFinished = false;

    private String backgroundColor;
    private String thumbnail;

    protected Meeting() {
    }


    // ***** 필드 메서드 *****
    public void changeTitle(String title) {
        this.title = title != null ? title : this.title;
    }

    public void changeDescription(String description) {
        this.description = description != null ? description : this.description;
    }

    public void changeLocation(String location) {
        this.location = location != null ? location : this.location;
    }

    public void changeCapacity(Integer capacity) {
        this.capacity = capacity != null ? capacity : this.capacity;
    }

    public void changeLocalDateTime(LocalDateTime meetingDate) {
        this.meetingDate = meetingDate != null ? meetingDate : this.meetingDate;
    }

    public void changeIsFinished() { this.isFinished = true; }

    public void changeBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor != null ? backgroundColor : this.backgroundColor;
    }

    public void changeThumbnail(String thumbnail) {
        this.thumbnail = thumbnail != null ? thumbnail : this.thumbnail;
    }

    public void changePayment(Integer payment) {this.payment = payment != null ? payment : this.payment;}

    public void changeIsSharable(Boolean isSharable) {this.isSharable = isSharable != null ? isSharable : this.isSharable;}
    public void addMeetingPictureUrls(List<String> meetingPictureUrls) {
        for (String meetingPictureUrl : meetingPictureUrls) {
            getMeetingPictureUrls().add(meetingPictureUrl);
        }
    }

    public void updateMeeting(Category category, Integer payment, List<String> meetingPictureUrls, String title, String description, String location, Integer capacity, LocalDateTime meetingDates, Boolean isSharable) {
        changeCategory(category);
        changePayment(payment);
        addMeetingPictureUrls(meetingPictureUrls);
        changeTitle(title);
        changeDescription(description);
        changeLocation(location);
        changeCapacity(capacity);
        changeLocalDateTime(meetingDates);
        changeIsSharable(isSharable);
    }

    // ***** 연관 메서드 *****
    public void ownerMember(Member member) {
        this.creator = member;
    }

    public void changeTicket(Ticket ticket) {
        this.ticket = ticket;
        ticket.changeMeeting(this);
    }

    public void changeCategory(Category category) {
        this.category = category;
    }

    public void addParticipants(MemberMeeting memberMeeting) throws BadRequestException {
        if(capacity == getParticipants().size()){
            throw new BadRequestException("인원이 다 찼습니다.");
        }
        getParticipants().add(memberMeeting);
    }

    public static Meeting createMeeting(
            Category category,
            Member creator,
            List<String> meetingPictureUrls,
            String title,
            String description,
            String location,
            Integer capacity,
            Integer payment,
            LocalDateTime meetingDate,
            Boolean isSharable
    ) {
        Meeting meeting = Meeting.builder()
                .title(title)
                .payment(payment)
                .description(description)
                .location(location)
                .capacity(capacity)
                .meetingDate(meetingDate)
                .isSharable(isSharable)
                .isFinished(false)
                .backgroundColor("#808080")
                .thumbnail("https://test-bukkit-240415.s3.ap-northeast-2.amazonaws.com/Thumbnails/Template-240424/Placeholder_1.png")
                .build();

        meeting.ownerMember(creator);
        meeting.addMeetingPictureUrls(meetingPictureUrls);
        meeting.changeCategory(category);
        return meeting;
    }

    public void setParticipants(List<MemberMeeting> participants) {
        this.participants.addAll(participants);
    }

    public void deleteMemberMeeting(MemberMeeting... memberMeeting) {
        getParticipants().removeAll(Arrays.asList(memberMeeting));
    }
}
