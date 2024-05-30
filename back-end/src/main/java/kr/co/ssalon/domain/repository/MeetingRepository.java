package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting,Long>, MeetingRepositoryCustom {

    @Query("SELECT m FROM Meeting m WHERE m.isFinished = false")
    List<Meeting> findAllUnfinishedMeetings();
}
