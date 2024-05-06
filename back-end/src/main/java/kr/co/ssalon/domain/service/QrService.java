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
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.coyote.BadRequestException;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QrService {

    private final MemberMeetingRepository memberMeetingRepository;
    private final MemberService memberService;
    private final MeetingService meetingService;
    private final MemberMeetingService memberMeetingService;

    @Transactional
    public String getQrLink(String username, Long moimId) throws BadRequestException {
        Meeting meeting = meetingService.findMeeting(moimId);
        Member member = memberService.findMember(username);
        MemberMeeting memberMeeting = memberMeetingService.findByMemberAndMeeting(member, meeting);

        try {
            return memberMeeting.getKey();

        } catch (NullPointerException e) {
            String randomStr = RandomStringUtils.random(200, true, true);

            memberMeeting.settingKey(randomStr);

            memberMeetingRepository.save(memberMeeting);

            return randomStr;
        }
    }

    @Transactional
    public boolean checkQrLink(Long moimId, String key) throws BadRequestException {
        Meeting meeting = meetingService.findMeeting(moimId);
        List<MemberMeeting> memberMeetingList = meeting.getParticipants();

        try {
            for(MemberMeeting memberMeeting : memberMeetingList) {
                String savedKey = memberMeeting.getKey();

                if(savedKey.equals(key)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new BadRequestException("QR 코드 비교 중 오류가 발생했습니다.");
        }
    }
}

