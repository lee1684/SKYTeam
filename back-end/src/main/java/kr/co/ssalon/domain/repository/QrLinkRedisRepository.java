package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.MemberMeeting;
import kr.co.ssalon.domain.entity.QrLink;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface QrLinkRedisRepository extends CrudRepository<QrLink, String> {

}