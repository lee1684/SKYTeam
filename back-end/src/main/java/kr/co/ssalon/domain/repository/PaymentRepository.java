package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    void deleteByMeetingId(Long moimId);

    Payment findByMeetingId(Long moimId);
}
