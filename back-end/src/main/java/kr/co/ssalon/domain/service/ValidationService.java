package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.*;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ValidationService {
    public static Member validationMember(Optional<Member> member) throws BadRequestException {
        if (member.isPresent()) {
            return member.get();
        } else
            throw new BadRequestException("해당 회원을 찾을 수 없습니다");
    }

    public static Meeting validationMeeting(Optional<Meeting> meeting) throws BadRequestException {
        if (meeting.isPresent()) {
            return meeting.get();
        } else
            throw new BadRequestException("해당 모임을 찾을 수 없습니다");
    }

    public static Category validationCategory(Optional<Category> category) throws BadRequestException {
        if (category.isPresent()) {
            return category.get();
        } else
            throw new BadRequestException("해당 카테고리를 찾을 수 없습니다");
    }

    public static MemberMeeting validationMemberMeeting(Optional<MemberMeeting> findMemberMeeting) throws BadRequestException {
        if (findMemberMeeting.isPresent()) {
            return findMemberMeeting.get();
        } else
            throw new BadRequestException("회원이 참여하고 있는 모임을 찾을 수 없습니다.");
    }

    public static Ticket validationTicket(Optional<Ticket> ticket) throws BadRequestException {
        if (ticket.isPresent()) {
            return ticket.get();
        } else
            throw new BadRequestException("해당 티켓을 찾을 수 없습니다");
    }

    public static Payment validationPayment(Optional<Payment> payment) throws BadRequestException {
        if (payment.isPresent()) {
            return payment.get();
        } else
            throw new BadRequestException("해당 결제를 찾을 수 없습니다");
    }

    public static Report validationReport(Optional<Report> report) throws BadRequestException {
        if (report.isPresent()) {
            return report.get();
        } else
            throw new BadRequestException("해당 신고를 찾을 수 없습니다");
    }

    public void validationAdmin(String role) throws BadRequestException {
        if (!role.equals("ROLE_ADMIN")) {
            throw new BadRequestException("관리자가 아닙니다.");
        }
    }
}
