package kr.co.ssalon.domain.service;

import kr.co.ssalon.domain.entity.Meeting;
import kr.co.ssalon.domain.entity.Member;
import kr.co.ssalon.domain.repository.MeetingRepository;
import kr.co.ssalon.domain.repository.MemberRepository;
<<<<<<< HEAD
import lombok.extern.slf4j.Slf4j;
=======
>>>>>>> develop
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

<<<<<<< HEAD
@Slf4j
=======
>>>>>>> develop
@Service
public class RecommendService {

    private final MemberRepository memberRepository;
    private final AwsLambdaService awsLambdaService;
    private final MeetingRepository meetingRepository;

    @Autowired
    public RecommendService(AwsLambdaService awsLambdaService, MemberRepository memberRepository, MeetingRepository meetingRepository) {
        this.awsLambdaService = awsLambdaService;
        this.memberRepository = memberRepository;
        this.meetingRepository = meetingRepository;
    }

    @Async
    public void updateMemberEmbedding(Member member) {

        Long memberId = member.getId();
        String memberName = member.getNickname();
        StringBuilder prompt = new StringBuilder();

        switch (member.getGender()) {
            case 'M':
                prompt.append("저는 남성입니다. ");
                break;
            case 'F':
                prompt.append("저는 여성입니다. ");
                break;
        }
        prompt.append("저는 ").append(member.getAddress()).append("에서 활동합니다. 저는 ");
        for (String interest : member.getInterests()) {
            prompt.append(interest).append(", ");
        }
        prompt.append("이외 흥미 있는 여러가지 것들을 좋아합니다. ");
        prompt.append(member.getIntroduction());

        String moimResult = "1,2,3,4,5,6,7,8,9,10";
        String categoryResult = "1,2,3,4,5,6,7,8,9,10";
        try {
<<<<<<< HEAD
            log.info("Trying to update user embedding");
            awsLambdaService.updateUserEmbedding(memberId, memberName, prompt.toString());
        } catch (Exception e) {
            log.error("Exception in updateMemberEmbedding", e);
=======
            awsLambdaService.updateUserEmbedding(memberId, memberName, prompt.toString());
>>>>>>> develop
        } finally {
            moimResult = awsLambdaService.fetchMoimRecommendation(memberId).block();
            categoryResult = awsLambdaService.fetchCategoryRecommendation(memberId).block();
        }

        member.changeMeetingRecommendation(moimResult);
        member.changeCategoryRecommendation(categoryResult);

        memberRepository.save(member);
<<<<<<< HEAD

        log.info("updateMemberEmbedding finished for member: {}", member.getId());
=======
>>>>>>> develop
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
<<<<<<< HEAD
=======

    @Async
    public void updateMoimEmbeddingAll() {

        List<Meeting> allMeetings = meetingRepository.findAll();

        for (Meeting meeting : allMeetings) {
            Long moimId = meeting.getId();
            String moimTitle = meeting.getTitle();
            StringBuilder prompt = new StringBuilder();

            prompt.append("우리 모임은 ").append(meeting.getCategory()).append(" 모임입니다. ");
            prompt.append("우리 모임은 ").append(meeting.getLocation()).append("에서 열립니다. ");
            prompt.append(meeting.getDescription());

            awsLambdaService.updateMoimEmbedding(moimId, moimTitle, prompt.toString());
        }
    }
>>>>>>> develop
}
