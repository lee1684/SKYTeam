package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByUsername(String username);

    Member findMemberById(long id);

    void update(Member member);

    @Query("SELECT m FROM Member m WHERE m.username = :username")
    Member getByUsername(String username);
}
