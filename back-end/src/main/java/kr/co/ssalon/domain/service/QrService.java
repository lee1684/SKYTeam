package kr.co.ssalon.domain.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberMeeting;
import kr.co.ssalon.domain.repository.MeetingRepository;
import kr.co.ssalon.domain.repository.MemberMeetingRepository;
import kr.co.ssalon.domain.repository.MemberRepository;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;
import org.apache.coyote.BadRequestException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Service
@RestController
@Transactional
public class QrService {

    @Autowired
    private RedisTemplate<String, byte[]> redisTemplate;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberMeetingRepository memberMeetingRepository;

    public byte[] getQrLink(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, Long moimId, Long userId) throws BadRequestException {
        Meeting meeting = meetingRepository.getReferenceById(moimId);
        Member member = memberRepository.getReferenceById(userId);

        MemberMeeting memberMeeting = memberMeetingRepository.findByMemberAndMeeting(member, meeting);
        String key = memberMeeting.getQrLink().getQrLink();
        if (StringUtils.isEmpty(key)) {
            String randomStr = RandomStringUtils.random(200, true, true);

            // QR 이미지 생성
            byte[] qrImage = generateQRCode(randomStr);

            // Redis에 QR 이미지 저장
            String redisKey = "QR_" + memberMeeting.getId();
            redisTemplate.opsForValue().set(redisKey, qrImage); // 예시로 1일 동안 유지

            // MemberMeeting에 URL 저장
            memberMeeting.getQrLink().setQrLink(redisKey);
            memberMeetingRepository.save(memberMeeting);

            return qrImage;
        } else {
            // 이미 URL이 존재할 경우 해당 이미지를 가져와 반환
            return redisTemplate.opsForValue().get(key);
        }
    }

    @Transactional
    public boolean checkQrLink(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, Long moimId, Long userId, MultipartFile file) throws BadRequestException {
        Meeting meeting = meetingRepository.getReferenceById(moimId);
        Member member = memberRepository.getReferenceById(userId);

        MemberMeeting memberMeeting = memberMeetingRepository.findByMemberAndMeeting(member, meeting);
        String redisKey = memberMeeting.getQrLink().getQrLink();

        try {
            byte[] savedImage = redisTemplate.opsForValue().get(redisKey);
            byte[] uploadedImage = file.getBytes();

            // 이미지 비교
            return Arrays.equals(savedImage, uploadedImage);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BadRequestException("QR 코드 비교 중 오류가 발생했습니다.");
        }
    }

    // QR 코드 생성 메서드
    private byte[] generateQRCode(String randomStr) {
        try {
            BitMatrix encode = new MultiFormatWriter().encode(randomStr, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(encode, "PNG", out);
            return out.toByteArray();
        } catch (Exception e) {
            // QR 코드 생성에 실패한 경우 예외 처리
            e.printStackTrace();
            return new byte[0];
        }
    }
}

