package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting,Long>, MeetingRepositoryCustom {


}
