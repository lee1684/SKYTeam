package kr.co.ssalon.kakaopay;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KakaoPayProperties {
    public final String secretKey;

    public final String cid;

    public final String readyUrl;

    public final String approveUrl;
    public final String cancelUrl;
    public final String partnerUserId;

    @Autowired
    public KakaoPayProperties(@Value("${kakaopay.secret_key}") String secretKey, @Value("${kakaopay.cid}") String cid,
                              @Value("${kakaopay.ready_url}") String readyUrl,
                              @Value("${kakaopay.approve_url}") String approveUrl,
                              @Value("${kakaopay.cancel_url}") String cancelUrl,
                            @Value("${kakaopay.partner_user_id}") String partnerUserId) {
        this.secretKey = secretKey;
        this.cid = cid;
        this.readyUrl = readyUrl;
        this.approveUrl = approveUrl;
        this.cancelUrl = cancelUrl;
        this.partnerUserId = partnerUserId;

    }
}
