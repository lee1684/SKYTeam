package kr.co.ssalon.web.dto;

import kr.co.ssalon.domain.entity.Payment;
import kr.co.ssalon.domain.entity.QrLink;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QrLinkDTO {

    private String qrKey;

    public QrLinkDTO(QrLink qrLink) {
        this.qrKey = qrLink.getQrKey();
    }

}
