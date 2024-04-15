package kr.co.ssalon;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("local")
@Component
@RequiredArgsConstructor
public class testDataInit {

    private final InitTest initTest;

    @PostConstruct
    public void init() {
        initTest.initData();
    }
    @Component
    static class InitTest{
        @Transactional
        public void initData() {

        }


    }

}
