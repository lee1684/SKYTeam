package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberMeeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberMeetingRepository extends JpaRepository<MemberMeeting,Long> {
    @Query("SELECT mm FROM MemberMeeting mm WHERE mm.member = :member AND mm.meeting = :meeting")
    MemberMeeting findByMemberAndMeeting(Member member, Meeting meeting);
}
