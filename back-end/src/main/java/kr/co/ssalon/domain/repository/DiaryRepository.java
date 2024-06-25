package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Diary;
import kr.co.ssalon.domain.entity.MemberMeeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Optional<Diary> findByMemberMeeting(MemberMeeting memberMeeting);
}
