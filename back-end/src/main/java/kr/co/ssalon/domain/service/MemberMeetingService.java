package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.dto.MeetingForm;
import kr.co.ssalon.domain.dto.ResignReasonForm;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberMeeting;
import kr.co.ssalon.domain.repository.MeetingRepository;
import kr.co.ssalon.domain.repository.MemberMeetingRepository;
import kr.co.ssalon.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

import java.io.WriteAbortedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberMeetingService {

    private final MeetingRepository meetingRepository;
    private final MemberMeetingRepository memberMeetingRepository;
    private final MemberRepository memberRepository;

}