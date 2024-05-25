package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberMeeting;
import kr.co.ssalon.domain.entity.Message;
import kr.co.ssalon.domain.repository.MessageRepository;
import kr.co.ssalon.web.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {

    private final MemberMeetingService memberMeetingService;
    private final MessageRepository messageRepository;

    @Transactional
    public MessageDTO saveMessage(Member member, Meeting meeting, String message) throws BadRequestException {
        MemberMeeting memberMeeting = memberMeetingService.findByMemberAndMeeting(member, meeting);
        Message messageEntity = Message.createMessage(memberMeeting, message);
        messageEntity = messageRepository.save(messageEntity);

        return new MessageDTO(messageEntity);
    }

    @Transactional
    public List<MessageDTO> getMoimChatHistory(Long moimId) {
        return messageRepository.findAllByMeetingId(moimId).stream().map(MessageDTO::new).toList();
    }

    public List<MessageDTO> getMyChatHistory(Long memberId) {
        return messageRepository.findAllByMemberId(memberId).stream().map(MessageDTO::new).toList();
    }
}
