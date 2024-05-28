package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class RecommendService {

    private final MemberRepository memberRepository;
    private final AwsLambdaService awsLambdaService;

    @Autowired
    public RecommendService(AwsLambdaService awsLambdaService, MemberRepository memberRepository) {
        this.awsLambdaService = awsLambdaService;
        this.memberRepository = memberRepository;
    }

    @Async
    public void updateMemberEmbedding(Member member) {

        Long memberId = member.getId();
        String memberName = member.getNickname();
        StringBuilder prompt = new StringBuilder();

        prompt.append("저는 ").append(member.getGender()).append("입니다. ");
        prompt.append("저는 ").append(member.getAddress()).append("에서 활동합니다. 저는 ");
        for (String interest : member.getInterests()) {
            prompt.append(interest).append(", ");
        }
        prompt.append("이외 흥미 있는 여러가지 것들을 좋아합니다. ");
        prompt.append(member.getIntroduction());

        awsLambdaService.updateUserEmbedding(memberId, memberName, prompt.toString());
        String moimResult = awsLambdaService.fetchMoimRecommendation(memberId).block();
        String categoryResult = awsLambdaService.fetchCategoryRecommendation(memberId).block();

        member.changeMeetingRecommendation(moimResult);
        member.changeCategoryRecommendation(categoryResult);

        memberRepository.save(member);
    }

    @Async
    public void updateMoimEmbedding(Meeting meeting) {

        Long moimId = meeting.getId();
        String moimTitle = meeting.getTitle();
        StringBuilder prompt = new StringBuilder();

        prompt.append("우리 모임은 ").append(meeting.getCategory()).append(" 모임입니다. ");
        prompt.append("우리 모임은 ").append(meeting.getLocation()).append("에서 열립니다. ");
        prompt.append(meeting.getDescription());

        awsLambdaService.updateMoimEmbedding(moimId, moimTitle, prompt.toString());
    }
}
