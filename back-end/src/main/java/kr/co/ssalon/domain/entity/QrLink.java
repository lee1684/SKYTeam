package kr.co.ssalon.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

    public static QrLink createQrLink(MemberMeeting memberMeeting, String qrKey) {
        return QrLink.builder()
                .memberMeeting(memberMeeting)
                .qrKey(qrKey)
                .build();
    }
}
