package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryCustom  {

    Optional<Member> findByUsername(String username);

    List<Member> findByBlackReasonIsNotNull();

}
