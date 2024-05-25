package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

}
