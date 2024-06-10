package kr.co.ssalon.domain.repository;

import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.entity.Report;
import kr.co.ssalon.web.dto.BlackListSearchCondition;
import kr.co.ssalon.web.dto.ReportSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

    Page<Member> getBlackList(BlackListSearchCondition blackListSearchCondition, Pageable pageable);
}
