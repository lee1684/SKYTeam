package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.Payment;
import kr.co.ssalon.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment findPayment(Long paymentId) throws BadRequestException {
        Optional<Payment> findPayment = paymentRepository.findById(paymentId);
        Payment payment = ValidationService.validationPayment(findPayment);
        return payment;
    }


}
