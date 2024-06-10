package kr.co.ssalon.domain.repository;

<<<<<<< HEAD
import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findPaymentByMemberAndMeeting(Member member, Meeting meeting);

    List<Payment> findAllByMeetingIdAndDtype(Long moimid, String dtype);

    List<Payment> findAllByMemberIdAndDtype(Long memberId, String dtype);
    List<Payment> findAllByDtype(String dtype);

=======
import kr.co.ssalon.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
>>>>>>> develop
}
