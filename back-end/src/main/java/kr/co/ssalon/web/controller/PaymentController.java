package kr.co.ssalon.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.Payment;
import kr.co.ssalon.domain.service.MeetingService;
import kr.co.ssalon.domain.service.MemberService;
import kr.co.ssalon.domain.service.PaymentService;
import kr.co.ssalon.domain.service.ValidationService;
import kr.co.ssalon.kakaopay.KakaoPayProperties;
import kr.co.ssalon.kakaopay.PayService;
import kr.co.ssalon.kakaopay.dto.*;
import kr.co.ssalon.oauth2.CustomOAuth2Member;
import kr.co.ssalon.web.dto.JsonResult;
import kr.co.ssalon.web.dto.PaymentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final MeetingService meetingService;
    private final MemberService memberService;
    private final KakaoPayProperties kakaoPayProperties;
    private final CommonPayInfo commonPayInfo = new CommonPayInfo();

    // 개최자 광고 게시 비용 결제
    @PostMapping("/advertisements/{moimId}")
    public ResponseEntity<?> advertisePaymentReady(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable("moimId") Long moimId) throws Exception {
        String username = customOAuth2Member.getUsername();
        Member member = memberService.findMember(username);
        Meeting meeting = meetingService.findMeeting(moimId);
        // 개설자 검증
        ValidationService.validationCreatorMoim(meeting, member);
        // 결제 중복 검증
        paymentService.checkPayment(username, moimId);
        int advertiseTotalPaymentSize = paymentService.getAdvertiseTotalPayment().size();
        PayService payService = new PayService(kakaoPayProperties);
        KakaopayReadyRequestDto dto = KakaopayReadyRequestDto.builder()
                .cid(kakaoPayProperties.getCid())
                .partnerOrderId(String.valueOf(advertiseTotalPaymentSize))
                .partnerUserId(kakaoPayProperties.getPartnerUserId())
                .itemName("상단 광고")
                .quantity(1)
                .totalPayment(10000)
                .taxFreeAmount(0)
                .approvalUrl("http://localhost:8080/advertise/success")
                .cancelUrl("http://localhost:8080/advertise/fail")
                .failUrl("http://localhost:8080/advertise/cancel")
                .build();
        KakaopayReadyResponseDto kakaopayReadyResponseDto = payService.kakaoPayReady(dto);
        commonPayInfo.setTid(kakaopayReadyResponseDto.getTid());
        commonPayInfo.setMoimId(moimId);
        commonPayInfo.setUsername(username);
        log.info("{}", kakaopayReadyResponseDto);
        return ResponseEntity.ok().body(kakaopayReadyResponseDto);
    }

    // 광고 결제 승인
    @GetMapping("/advertise/success")
    public void advertisePaymentApprove(@RequestParam("pg_token") String pgToken, HttpServletResponse response) throws Exception {
        log.info("{}", pgToken);
        // 결제 내역 저장 후 회원가입 -> 가입 후 바로 리다이렉트 << 프론트 처리
        PayService payService = new PayService(kakaoPayProperties);
        int advertiseTotalPaymentSize = paymentService.getAdvertiseTotalPayment().size();
        KakaopayApproveRequestDto dto = KakaopayApproveRequestDto.builder()
                .tid(commonPayInfo.getTid())
                .cid(kakaoPayProperties.getCid())
                .partnerOrderId(String.valueOf(advertiseTotalPaymentSize))
                .partnerUserId(kakaoPayProperties.getPartnerUserId())
                .pgToken(pgToken)
                .build();
        KakaopayApproveResponseDto kakaopayApproveResponseDto = payService.kakaoPayApprove(dto);
        String username = commonPayInfo.getUsername();

        // 프론트 도메인으로 변경 필요
        String redirectUrl = "http://localhost:5173";
        paymentService.checkPayment(username, commonPayInfo.getMoimId());
        Long id = paymentService.completeAdvertisePayment(username, commonPayInfo.getMoimId(), "광고 가입 완료", kakaopayApproveResponseDto.returnTotal(), commonPayInfo.getTid());
        response.sendRedirect(redirectUrl);
    }

    // 광고 게시 비용 환불
    @PostMapping("/advertisements/billings/{userId}/{paymentId}")
    public ResponseEntity<?> advertisePaymentCancel(@PathVariable("userId") Long userId, @PathVariable("paymentId") Long paymentId) throws Exception {
        Payment payment = paymentService.getPayment(paymentId);
        PayService payService = new PayService(kakaoPayProperties);
        KakaopayCancelRequestDto dto = KakaopayCancelRequestDto.builder()
                .cid(kakaoPayProperties.getCid())
                .tid(payment.getTid())
                .cancelAmount(payment.getAmount())
                .cancelTaxFreeAmount(0)
                .build();
        KakaopayCancelResponseDto kakaopayCancelResponseDto = payService.kakaoPayCancel(dto);
        paymentService.completeAdvertiseRefund(userId, paymentId);
        return ResponseEntity.ok().body(kakaopayCancelResponseDto);
    }

    // 광고 게시 비용 결제 내역 조회
    @GetMapping("/advertisements/billings/{userId}")
    public ResponseEntity<?> getAdvertisePayments(@PathVariable("userId") Long userId) {
        List<Payment> paymentAll = paymentService.getAdvertisePaymentAll(userId);
        List<PaymentDTO> collect = paymentAll.stream().map(PaymentDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(new JsonResult<>(collect));
    }


    // 모임비가 있는 모임 참가 비용 결제 -> 결제 준비
    @PostMapping("/moims/{moimId}/billings")
    public ResponseEntity<?> moimPaymentReady(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable("moimId") Long moimId) throws Exception {
        String username = customOAuth2Member.getUsername();
        Meeting meeting = meetingService.findMeeting(moimId);
        paymentService.checkPayment(username, moimId);
        int moimTotalPaymentsSize = paymentService.getMoimsTotalPayment().size();
        PayService payService = new PayService(kakaoPayProperties);
        KakaopayReadyRequestDto dto = KakaopayReadyRequestDto.builder()
                .cid(kakaoPayProperties.getCid())
                .partnerOrderId(String.valueOf(moimTotalPaymentsSize))
                .partnerUserId(kakaoPayProperties.getPartnerUserId())
                .itemName(meeting.getTitle())
                .quantity(1)
                .totalPayment(meeting.getPayment())
                .taxFreeAmount(0)
                .approvalUrl("https://ssalon.co.kr/payment/success")
                .cancelUrl("https://ssalon.co.kr/payment/fail")
                .failUrl("https://ssalon.co.kr/payment/cancel")
                .build();
        KakaopayReadyResponseDto kakaopayReadyResponseDto = payService.kakaoPayReady(dto);
        commonPayInfo.setTid(kakaopayReadyResponseDto.getTid());
        commonPayInfo.setMoimId(moimId);
        commonPayInfo.setUsername(username);
        log.info("{}", kakaopayReadyResponseDto);
        return ResponseEntity.ok().body(kakaopayReadyResponseDto);

    }

    // 모임 결제 승인
    @GetMapping("/payment/success")
    public void moimPaymentApprove(@RequestParam("pg_token") String pgToken, HttpServletResponse response) throws Exception {
        log.info("{}", pgToken);
        // 결제 내역 저장 후 회원가입 -> 가입 후 바로 리다이렉트 << 프론트 처리
        PayService payService = new PayService(kakaoPayProperties);
        int moimTotalPaymentsSize = paymentService.getMoimsTotalPayment().size();
        KakaopayApproveRequestDto dto = KakaopayApproveRequestDto.builder()
                .tid(commonPayInfo.getTid())
                .cid(kakaoPayProperties.getCid())
                .partnerOrderId(String.valueOf(moimTotalPaymentsSize))
                .partnerUserId(kakaoPayProperties.getPartnerUserId())
                .pgToken(pgToken)
                .build();
        KakaopayApproveResponseDto kakaopayApproveResponseDto = payService.kakaoPayApprove(dto);
        String username = commonPayInfo.getUsername();
        // 프론트 도메인으로 변경 필요
        String redirectUrl = "https://ssalon.co.kr";
        paymentService.checkPayment(username, commonPayInfo.getMoimId());
        Long id = paymentService.completeMoimPayment(username, commonPayInfo.getMoimId(), "모임 가입 완료", kakaopayApproveResponseDto.returnTotal(), commonPayInfo.getTid());
        meetingService.join(username, commonPayInfo.getMoimId());
        response.sendRedirect(redirectUrl);
    }

    // 모임 참가 비용 환불 (모임 참가 취소, 강튀, 모임 삭제 시 후에 작동되는 부분)
    @PostMapping("/moims/{moimId}/billings/{paymentId}")
    public ResponseEntity<?> moimPaymentCancel(@AuthenticationPrincipal CustomOAuth2Member customOAuth2Member, @PathVariable("moimId") Long moimid, @PathVariable("paymentId") Long paymentId) throws Exception {
        Payment payment = paymentService.getPayment(paymentId);
        PayService payService = new PayService(kakaoPayProperties);
        String username = customOAuth2Member.getUsername();
        KakaopayCancelRequestDto dto = KakaopayCancelRequestDto.builder()
                .cid(kakaoPayProperties.getCid())
                .tid(payment.getTid())
                .cancelAmount(payment.getAmount())
                .cancelTaxFreeAmount(0)
                .build();
        KakaopayCancelResponseDto kakaopayCancelResponseDto = payService.kakaoPayCancel(dto);
        // 환불
        paymentService.completeMoimRefund(username, moimid);
        return ResponseEntity.ok().body(kakaopayCancelResponseDto);
    }

    // 해당 모임 참가 비용 결제 내역 조회
    @GetMapping("/moims/{moimId}/billings")
    public ResponseEntity<?> getMoimPayments(@PathVariable("moimId") Long moimId) {
        List<Payment> paymentAll = paymentService.getMoimPaymentAll(moimId);
        List<PaymentDTO> collect = paymentAll.stream().map(PaymentDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(new JsonResult<>(collect));
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class CommonPayInfo {
        private String tid;
        private String username;
        private Long moimId;
    }

}
