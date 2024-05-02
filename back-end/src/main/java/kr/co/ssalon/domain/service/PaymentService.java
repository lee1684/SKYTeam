package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.Payment;
import kr.co.ssalon.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment findPayment(Long id) throws BadRequestException {
        Optional<Payment> findPayment = paymentRepository.findById(id);
        Payment payment = validationPayment(findPayment);
        return payment;
    }

    private Payment validationPayment(Optional<Payment> payment) throws BadRequestException {
        if (payment.isPresent()) {
            return payment.get();
        }else
            throw new BadRequestException("해당 회원을 찾을 수 없습니다");
    }

}
