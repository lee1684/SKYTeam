package kr.co.ssalon.domain.entity;


import org.springframework.data.redis.core.RedisHash;
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
@RedisHash(value = "QrLink")
public class QrLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qr_link_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private MemberMeeting memberMeeting;

    private String QrLink;

    protected QrLink() {

    }

    public static QrLink createQrLink(MemberMeeting memberMeeting, String qrLink) {
        QrLink qrLinkEntity = new QrLink();
        qrLinkEntity.setMemberMeeting(memberMeeting);
        qrLinkEntity.setQrLink(qrLink);
        return qrLinkEntity;
    }
}
