# :dancers: SSALON: 증표 기반 일회성 모임관리 서비스
![SSALON 메인 이미지](https://i.imgur.com/OVJeDPd.png)

## 🔍 프로젝트 개요 (Overview)
최근 사람들은 새로운 곳에 소속되어 새로운 관계가 생기는 것을 꺼려하는 등 집단주의 보다는 개인주의적 성향이 매우 강한 편입니다. 이러한 배경에서 일회성 모임에 대한 수요는 계속 해서 늘어나고 있는 상황입니다. 하지만 참가자 인증의 불편함, 모임 홍보의 어려움, 추가적인 추억 보존 수단 수요, 불만족스러운 서비스 이용자 경험 등으로 인해 현재 시장에 나와있는 서비스들은 소비자들의 수요를 충분히 만족시키지 못하고 있다고 판단됩니다.  

위의 문제를 해결하기 위한 <strong>SSALON'(이하 '살롱')</strong>은 '인터랙티브 3D 증표' 기반의 일회성 모임 커뮤니티를 제공하는 웹 서비스입니다. 사용자는 증표를 바탕으로 모임의 개최, 참가, 관리, 추억, 인증 등 일회성 모임과 관련된 모든 활동을 편리하게 이용할 수 있습니다. 특히, 증표를 이용한 추억 기록은 타 서비스와 비교되는 특장점(POD)으로 '살롱' 만의 '3D 증표'를 이용하여 보다 쉽고, 재미있는 일회성 모임 경험을 제공받을 수 있을 것입니다.  

이 프로젝트는 2024년 1학기 아주대학교 소프트웨어학과 SW캡스톤디자인 수업의 일환으로 11팀 SKYTeam 팀이 2024년 3월부터 6월까지 개발 진행하였습니다. 

## 🪄 프로젝트 제안 (Project Introduction)
### 문제 정의 (Problems We Found)
![문제정의 1](https://i.imgur.com/zHUR0uC.png)  
![문제정의 2](https://i.imgur.com/JMYwDnR.png)

### 살롱이 제안하는 해결책 (Solutions We Suggest)
![해결책](https://i.imgur.com/rCsnl35.png)

### 타겟과 이해관계자 (Targets & Stakeholders)
![타겟과 이해관계자](https://i.imgur.com/3idFo1P.png)

## 🌠 프로젝트 주요 기능 (Key Features)
### 3D 인터랙티브 증표
WebGL 라이브러리인 **Three.js**를 통해 증표를 렌더링하고, 이벤트 처리를 통해 상호작용 할 수 있도록 구현하였습니다. HTML Canvas 라이브러리인 **Fabric.js**를 통해 증표의 앞, 뒤를 꾸밀 수 있는 에디터를 개발했습니다.

### 모임/카테고리 추천 시스템
**OpenAI의 Embedding API**를 사용하여 사용자, 모임, 카테고리 정보를 임베딩합니다. 이를 MongoDB Atlas의 벡터 DB에 저장하여 신속한 유사도 계산 및 추천 결과를 도출하여 사용자 맞춤 모임 노출 및 카테고리 정렬이 가능합니다.

### 모임 참가자 간 채팅
**Web Socket** 구현 및 적용을 바탕으로 Web application과 Server간의 지속적이고 신속한 양방향 통신을 통해 모임 내 구성원 간 실시간 채팅 기능을 지원합니다.

### 모임 참가자 인증
**ZXing** 라이브러리와 **WebRTC**를 사용하여 구현하였으며 QR코드 제시 및 촬영을 통해 오프라인 모임 참가자 인증이 가능합니다. 

## 🔧 주요 기술스택 (Technology Stacks)
### 프론트엔드 (Front-End)
<table>
    <tr>
        <th><center>-</center></th>
        <th align="center"><center>이름</center></th>
        <th>사용 용도</th>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E" alt="JavaScript"></center></td>
        <td valign="">JavaScript</td>
        <td valign="">프론트엔드 주요 프로그래밍 언어</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/angular-%23DD0031.svg?style=for-the-badge&logo=angular&logoColor=white" alt="Angular"></center></td>
        <td valign="">Angular</td>
        <td valign="">사용자 페이지 구현</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB" alt="React"></center></td>
        <td valign="">React</td>
        <td valign="">관리자 페이지 구현</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/threejs-black?style=for-the-badge&logo=three.js&logoColor=white" alt="Three.js"></center></td>
        <td valign="">Three.js</td>
        <td valign="">3D 증표 구현</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white" alt="HTML Canvas"></center></td>
        <td valign="">Fabric.js</td>
        <td valign="">HTML Canvas 기반 라이브러리로 3D 증표 구현</td>
    </tr>
</table>

### 백엔드 (Back-End)
<table>
    <tr>
        <th><center>-</center></th>
        <th align="center"><center>이름</center></th>
        <th>사용 용도</th>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java"></center></td>
        <td valign="">Java</td>
        <td valign="">Spring Boot 주요 프로그래밍 언어</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/python-3670A0?style=for-the-badge&logo=python&logoColor=ffdd54" alt="Python"></center></td>
        <td valign="">Python</td>
        <td valign="">AWS Lambda 주요 프로그래밍 언어</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=Spring%20Boot&logoColor=white" alt="Spring Boot"></center></td>
        <td valign="">Spring Boot</td>
        <td valign="">서버 주요 로직 구현</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white" alt="AWS"></center></td>
        <td valign="">AWS Lambda</td>
        <td valign="">추천 시스템 관련 로직 구현</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white" alt="Spring Security"></center></td>
        <td valign="">Spring Security</td>
        <td valign="">역할 기반 권한 제어(Role-based Access Control)</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL"></center></td>
        <td valign="">MySQL</td>
        <td valign="">사용자 및 모임 정보 관리</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white" alt="Redis"></center></td>
        <td valign="">Redis</td>
        <td valign="">사용자 로그인 정보 관리</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white" alt="MongoDB"></center></td>
        <td valign="">MongoDB Atlas</td>
        <td valign="">추천 시스템의 벡터 DB로서<br>임베딩 정보 관리 및 벡터 유사도 검색 수행</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white" alt="AWS"></center></td>
        <td valign="">AWS EC2</td>
        <td valign="">서버 인스턴스 서빙</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white" alt="AWS"></center></td>
        <td valign="">AWS RDS</td>
        <td valign="">MySQL DB 인스턴스 서빙</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white" alt="AWS"></center></td>
        <td valign="">AWS ElasticCache</td>
        <td valign="">Redis DB 인스턴스 서빙</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white" alt="AWS"></center></td>
        <td valign="">AWS S3</td>
        <td valign="">이미지, JSON 등 정적 파일 관리</td>
    </tr>
</table>

### 외부 API (External API)
<table>
    <tr>
        <th><center>-</center></th>
        <th align="center"><center>이름</center></th>
        <th>사용 용도</th>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/OpenAI-74aa9c?style=for-the-badge&logo=openai&logoColor=white" alt="OpenAI"></center></td>
        <td valign="">OpenAI</td>
        <td valign="">Embedding API 활용하여 사용자/모임/카테고리 정보에서 임베딩 추출</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/kakao-ffcd00.svg?style=for-the-badge&logo=kakao&logoColor=black" alt="Kakao"></center></td>
        <td valign="">Kakao</td>
        <td valign="">소셜 로그인을 통한 OAuth 사용자 인증</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/google-4285F4.svg?style=for-the-badge&logo=google&logoColor=white" alt="Google"></center></td>
        <td valign="">Google</td>
        <td valign="">소셜 로그인을 통한 OAuth 사용자 인증</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/naver-03C75A.svg?style=for-the-badge&logo=naver&logoColor=white" alt="Naver"></center></td>
        <td valign="">Naver</td>
        <td valign="">소셜 로그인을 통한 OAuth 사용자 인증</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/kakaopay-ffcd00.svg?style=for-the-badge&logo=kakao&logoColor=black" alt="Kakaopay"></center></td>
        <td valign="">Kakaopay</td>
        <td valign="">서비스 이용 중 발생하는 결제 처리</td>
    </tr>
</table>

### 기타 도구 (Other Tools)
<table>
    <tr>
        <th><center>-</center></th>
        <th align="center"><center>이름</center></th>
        <th>사용 용도</th>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white" alt="Github"></center></td>
        <td valign="">Github</td>
        <td valign="">코드 버전 관리 및 공유</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white" alt="Github Actions"></center></td>
        <td valign="">Github Actions</td>
        <td valign="">Github의 코드 CI/CD Workflow 자동화</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white" alt="Slack"></center></td>
        <td valign="">Slack</td>
        <td valign="">Github Push 알림 / 기술 정보 공유 및 관리</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/kakaotalk-ffcd00.svg?style=for-the-badge&logo=kakaotalk&logoColor=black" alt="Slack"></center></td>
        <td valign="">KakaoTalk</td>
        <td valign="">팀원 간 일상 소통 및 긴급 공지</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/google-4285F4?style=for-the-badge&logo=google&logoColor=white" alt="Google Workspace"></center></td>
        <td valign="">Google Workspace</td>
        <td valign="">기획제안서, 회의록, Burn-down Chart 등 문서화 작업<br>Google Meet를 통한 비대면 회의</td>
    </tr>
</table>

## 🏗 시스템 구조 (System Architecture)
### 전체 시스템 개요 (System Overview)
![시스템 구성](https://i.imgur.com/3xstw23.png)

### 소프트웨어 아키텍쳐 (Software Architecture)
![소프트웨어 아키텍쳐](https://i.imgur.com/OhYg2cD.png)

## :running: 주요 기능 시연 (Feature Demostration)
AJOU SOFTCON 홈페이지의 [살롱 소개 페이지](https://softcon.ajou.ac.kr/works/works.asp?uid=1784)를 참조해주세요.  

## 👥 팀원 구성 (Team Member List)

|  이름  |      학과      |    담당    | 이메일 | 비고 |
| :----: | :------------: | :--------: | :--: | --- |
| [신윤석](https://github.com/sys7498) | 소프트웨어학과 | 프론트엔드 | sys7498@ajou.ac.kr | 팀장 |
| [김세현](https://github.com/NSRBSG) | 소프트웨어학과 | 프론트엔드 |  -  |  |
| [김지환](https://github.com/jihwankim129) | 소프트웨어학과 |   백엔드   |  -  |  |
| [양성호](https://github.com/SyingSHY) | 소프트웨어학과 |   백엔드   |  -  |  |
| [이정준](https://github.com/lee1684) | 소프트웨어학과 |   백엔드   |  -  |  |

+ 팀원 이름 클릭 시 해당 팀원의 Github 프로필로 연결됩니다.
+ Special Thanks to 한수현 멘토님

## 🎊 2024-1 AJOU SOFTCON 전시 (2024-1 AJOU SOFTCON Presentation)
[2024-1 AJOU SOFTCON](https://softcon.ajou.ac.kr/works/works.asp?uid=1784) : 이 링크를 클릭하여 SOFTCON 홈페이지 내 '살롱' 소개 페이지로 이동할 수 있습니다.

### 전시 포스터 (Presentation Poster)
![SSALON AJOU SOFTCON 포스터](https://i.imgur.com/yF4g8hi.png)

### 전시 소개 영상 (Presentation Video)
AJOU SOFTCON 홈페이지의 [살롱 소개 페이지](https://softcon.ajou.ac.kr/works/works.asp?uid=1784)를 참조해주세요.  

## 📜 라이선스 (License)
라이선스 내용입니다.
