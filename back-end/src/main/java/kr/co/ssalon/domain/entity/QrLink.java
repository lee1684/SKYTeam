package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class QrLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qr_link_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private MemberMeeting memberMeeting;

    private String qrKey;

    protected QrLink() {

    }

    // ***** 연관 메서드 *****
    public void settingMemberMeeting(MemberMeeting memberMeeting) {
        this.memberMeeting = memberMeeting;
        memberMeeting.settingQrLink(this);
    }

    public static QrLink createQrLink(MemberMeeting memberMeeting, String qrKey) {
        QrLink qrLink = QrLink.builder()
                .qrKey(qrKey)
                .build();
        qrLink.settingMemberMeeting(memberMeeting);
        return qrLink;
    }
}