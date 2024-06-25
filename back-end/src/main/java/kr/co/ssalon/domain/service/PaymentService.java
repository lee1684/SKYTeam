package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.Payment;
import kr.co.ssalon.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import kr.co.ssalon.domain.entity.*;
import kr.co.ssalon.domain.repository.MeetingRepository;
import kr.co.ssalon.domain.repository.MemberRepository;
import kr.co.ssalon.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final MeetingRepository meetingRepository;


    // 단건 결제 조회
    public Payment getPayment(Long paymentId) throws BadRequestException {
        Payment payment = findPayment(paymentId);
        return payment;
    }

    // 모임 결제 완료
    @Transactional
    public Long completeMoimPayment(String username, Long moimId, String purpose, Integer amount, String tid) throws BadRequestException {
        Member currentMember = findMember(username);
        Meeting meeting = findMeeting(moimId);
        Payment payment = MoimPayment.createMoimPayment(currentMember, meeting, purpose, amount, tid);
        Payment successPayment = paymentRepository.save(payment);
        return successPayment.getId();
    }
    // 광고 결제 완료
    @Transactional
    public Long completeAdvertisePayment(String username, Long moimId, String purpose, Integer amount, String tid) throws BadRequestException {
        Member currentMember = findMember(username);
        Meeting meeting = findMeeting(moimId);
        Payment payment = AdvertisePayment.createAdvertisePayment(currentMember, meeting, purpose, amount, tid);
        Payment successPayment = paymentRepository.save(payment);
        return successPayment.getId();
    }
    // 모임 결제 취소 (환불)
    @Transactional
    public void completeMoimRefund(String username, Long moimId) throws BadRequestException {
        Payment payment = findPayment(username, moimId);
        Member member = findMember(username);
        member.getPayments().remove(payment);
        paymentRepository.delete(payment);
    }
    // 광고 결제 취소 (환불)
    @Transactional
    public void completeAdvertiseRefund(Long userId, Long paymentId) throws BadRequestException {
        Payment payment = findPayment(paymentId);
        Member member = findMember(userId);
        member.getAdvertisements().remove(payment);
        paymentRepository.delete(payment);
    }
    // 결제 내역 중복 방지
    public void checkPayment(String username, Long moimId) throws BadRequestException {
        Member currentMember = findMember(username);
        Meeting meeting = findMeeting(moimId);
        Optional<Payment> findPayment = paymentRepository.findPaymentByMemberAndMeeting(currentMember, meeting);
        ValidationService.validationExistPayment(findPayment);
    }

    // 모임 참가 비용 결제 내역 조회 -> 참가자의 금전 지불 여부 확인
    public List<Payment> getMoimPaymentAll(Long moimId) {
        List<Payment> payments = paymentRepository.findAllByMeetingIdAndDtype(moimId,"M");
        return payments;
    }
    // 광고 게시 비용 결제 내역 조회
    public List<Payment> getAdvertisePaymentAll(Long userId) {
        List<Payment> payments = paymentRepository.findAllByMemberIdAndDtype(userId,"A");
        return payments;
    }

    // 모든 모임 결제 내역 조회
    public List<Payment> getMoimsTotalPayment() {
        return paymentRepository.findAllByDtype("M");
    }

    // 모든 광고 결제 내역 조회
    public List<Payment> getAdvertiseTotalPayment() {
        return paymentRepository.findAllByDtype("A");
    }



    public Payment findPayment(String username, Long moimId) throws BadRequestException {
        Member member = findMember(username);
        Meeting meeting = findMeeting(moimId);
        Optional<Payment> findPayment = paymentRepository.findPaymentByMemberAndMeeting(member, meeting);
        Payment payment = ValidationService.validationPayment(findPayment);
        return payment;
    }

    public Payment findPayment(Long paymentId) throws BadRequestException {
        Optional<Payment> findPayment = paymentRepository.findById(paymentId);
        Payment payment = ValidationService.validationPayment(findPayment);
        return payment;
    }

    private Member findMember(String username) throws BadRequestException {
        Optional<Member> findMember = memberRepository.findByUsername(username);
        Member member = ValidationService.validationMember(findMember);
        return member;
    }

    private Member findMember(Long userId) throws BadRequestException {
        Optional<Member> findMember = memberRepository.findById(userId);
        Member member = ValidationService.validationMember(findMember);
        return member;
    }

    private Meeting findMeeting(Long moimId) throws BadRequestException {
        Optional<Meeting> findMeeting = meetingRepository.findById(moimId);
        Meeting meeting = ValidationService.validationMeeting(findMeeting);
        return meeting;
    }

}
