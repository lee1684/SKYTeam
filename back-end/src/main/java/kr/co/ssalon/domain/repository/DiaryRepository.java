package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Diary;
import kr.co.ssalon.domain.entity.MemberMeeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Diary findByMemberMeeting(MemberMeeting memberMeeting);
}
