package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Meeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting,Long>, MeetingRepositoryCustom {

    @Query("SELECT m FROM Meeting m WHERE m.isFinished = false")
    List<Meeting> findAllUnfinishedMeetings();


    @Query("SELECT m FROM Meeting m WHERE m.title LIKE %:keyword% OR m.description LIKE %:keyword%")
    Page<Meeting> searchByTitleOrDescription(@Param("keyword") String keyword, Pageable pageable);


}
