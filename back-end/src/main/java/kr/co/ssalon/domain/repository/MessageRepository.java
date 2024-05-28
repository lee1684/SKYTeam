package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByMeetingId(Long moimId);

    List<Message> findAllByMemberId(Long memberId);
}
