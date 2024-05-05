package kr.co.ssalon.domain.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberMeeting;
import kr.co.ssalon.domain.entity.QrLink;
import kr.co.ssalon.domain.repository.MeetingRepository;
import kr.co.ssalon.domain.repository.MemberMeetingRepository;
import kr.co.ssalon.domain.repository.MemberRepository;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;
import org.apache.coyote.BadRequestException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QrService {

    private final RedisTemplate<String, byte[]> redisTemplate;
    private final MeetingRepository meetingRepository;
    private final MemberRepository memberRepository;
    private final MemberMeetingRepository memberMeetingRepository;
    private final MemberService memberService;
    private final MeetingService meetingService;
    private final MemberMeetingService memberMeetingService;

    @Transactional
    public byte[] getQrLink(String username, Long moimId) throws BadRequestException {
        Meeting meeting = meetingService.findMeeting(moimId);
        Member member = memberService.findMember(username);
        MemberMeeting memberMeeting = memberMeetingService.findByMemberAndMeeting(member, meeting);

        try {
            return redisTemplate.opsForValue().get(memberMeeting.getQrLink().getQrKey());

        } catch (NullPointerException e) {
            String randomStr = RandomStringUtils.random(200, true, true);

            // QR 이미지 생성
            byte[] qrImage = generateQRCode(randomStr);

            // Redis에 QR 이미지 저장
            String redisKey = "QR_" + memberMeeting.getId();
            redisTemplate.opsForValue().set(redisKey, qrImage);

            // MemberMeeting에 URL 저장
            QrLink qrlink = QrLink.createQrLink(memberMeeting, redisKey);
            return qrImage;
        }
    }

    @Transactional
    public boolean checkQrLink(Long moimId, byte[] key) throws BadRequestException {
        Meeting meeting = meetingService.findMeeting(moimId);
        List<MemberMeeting> memberMeetingList = meeting.getParticipants();

        try {
            for(MemberMeeting memberMeeting : memberMeetingList) {
                String redisKey = memberMeeting.getQrLink().getQrKey();

                byte[] savedImage = redisTemplate.opsForValue().get(redisKey);

                if(Arrays.equals(savedImage, key)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new BadRequestException("QR 코드 비교 중 오류가 발생했습니다.");
        }
    }

    // QR 코드 생성 메서드
    @Transactional
    public byte[] generateQRCode(String randomStr) throws BadRequestException {
        try {
            BitMatrix encode = new MultiFormatWriter().encode(randomStr, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(encode, "PNG", out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new BadRequestException("QR 코드 생성에 실패하였습니다.");
        }
    }
}

