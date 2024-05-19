package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.web.dto.MeetingSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepositoryCustom {

    // 모임 목록 조회
    // 모임 목록 필터 설정
    Page<Meeting> searchMoims(MeetingSearchCondition meetingSearchCondition, Member member, Pageable pageable);

    List<Meeting> findMeetingsByCategoryId(Long categoryId);



}
