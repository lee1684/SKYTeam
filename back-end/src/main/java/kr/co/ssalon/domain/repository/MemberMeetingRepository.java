package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.MemberMeeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberMeetingRepository extends JpaRepository<MemberMeeting,Long> {
}