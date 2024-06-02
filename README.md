<div align="center">
    
# :dancers: SSALON: 증표 기반 일회성 모임관리 서비스
![SSALON 메인 이미지](https://i.imgur.com/OVJeDPd.png)  
:house: **서비스 홈페이지** : [살롱 바로가기](https://ssalon.co.kr)  
:open_file_folder: **각 엔드별 상세 레포지토리** : [프론트엔드](./front-end) | [백엔드](./back-end)  
:key: **API Specification** : [Swagger](https://ssalon.co.kr/swagger-ui/index.html#/)

</div> <br><br><br><br>

## 🔍 프로젝트 개요 (Project Overview)
최근 사람들은 새로운 곳에 소속되어 새로운 관계가 생기는 것을 꺼려하는 등 집단주의 보다는 개인주의적 성향이 매우 강한 편입니다. 이러한 배경에서 일회성 모임에 대한 수요는 계속 해서 늘어나고 있는 상황입니다. 하지만 참가자 인증의 불편함, 모임 홍보의 어려움, 추가적인 추억 보존 수단 수요, 불만족스러운 서비스 이용자 경험 등으로 인해 현재 시장에 나와있는 서비스들은 소비자들의 수요를 충분히 만족시키지 못하고 있다고 판단됩니다.  

위의 문제를 해결하기 위한 <strong>SSALON'(이하 '살롱')</strong>은 '인터랙티브 3D 증표' 기반의 일회성 모임 커뮤니티를 제공하는 웹 서비스입니다. 사용자는 증표를 바탕으로 모임의 개최, 참가, 관리, 추억, 인증 등 일회성 모임과 관련된 모든 활동을 편리하게 이용할 수 있습니다. 특히, 증표를 이용한 추억 기록은 타 서비스와 비교되는 특장점(POD)으로 '살롱' 만의 '3D 증표'를 이용하여 보다 쉽고, 재미있는 일회성 모임 경험을 제공받을 수 있을 것입니다.  

이 프로젝트는 2024년 1학기 아주대학교 소프트웨어학과 SW캡스톤디자인 수업의 일환으로 11팀 SKYTeam 팀이 2024년 3월부터 6월까지 개발 진행하였습니다. 

<br><br>

## 🪄 프로젝트 제안 (Project Introduction)
### 문제 정의 (Problems We Found)
![문제정의 1](https://i.imgur.com/zHUR0uC.png)  
![문제정의 2](https://i.imgur.com/JMYwDnR.png)

### 살롱이 제안하는 해결책 (Solutions We Suggest)
![해결책](https://i.imgur.com/rCsnl35.png)

### 타겟과 이해관계자 (Targets & Stakeholders)
![타겟과 이해관계자](https://i.imgur.com/3idFo1P.png)

<br><br>

## 🌠 프로젝트 주요 기능 (Key Features)
### 3D 인터랙티브 증표
WebGL 라이브러리인 **Three.js**를 통해 증표를 렌더링하고, 이벤트 처리를 통해 상호작용 할 수 있도록 구현하였습니다. HTML Canvas 라이브러리인 **Fabric.js**를 통해 증표의 앞, 뒤를 꾸밀 수 있는 에디터를 개발했습니다.

### 모임/카테고리 추천 시스템 (_In progress_)
**OpenAI의 Embedding API**를 사용하여 사용자, 모임, 카테고리 정보를 임베딩합니다. 이를 MongoDB Atlas의 벡터 DB에 저장하고 신속한 유사도 계산을 통한 추천 결과 도출로 사용자 맞춤 모임 노출 및 카테고리 정렬을 구현합니다.

### 모임 참가자 간 채팅
**Web Socket** 구현 및 적용을 바탕으로 Web application과 Server간의 지속적이고 신속한 양방향 통신을 통해 모임 내 구성원 간 실시간 채팅 기능을 지원합니다.

### 실제 오프라인 모임 진행 시 모임 참가자 인증
**ZXing** 라이브러리와 **WebRTC**를 사용하여 구현하였으며 QR코드 제시 및 촬영을 통해 오프라인 모임 참가자 인증이 가능합니다. 

<br><br>

## 🔧 주요 기술스택 (Technology Stacks)
### 프론트엔드 (Front-End)
![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)
![Angular](https://img.shields.io/badge/angular-%23DD0031.svg?style=for-the-badge&logo=angular&logoColor=white)
![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB)
![Threejs](https://img.shields.io/badge/threejs-black?style=for-the-badge&logo=three.js&logoColor=white)
![HTML5](https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white)

### 백엔드 (Back-End)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Python](https://img.shields.io/badge/python-3670A0?style=for-the-badge&logo=python&logoColor=ffdd54)
<img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=Spring%20Boot&logoColor=white"/>
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)

### 외부 API (External API)
![OpenAI](https://img.shields.io/badge/OpenAI-74aa9c?style=for-the-badge&logo=openai&logoColor=white)
![Google](https://img.shields.io/badge/google-4285F4?style=for-the-badge&logo=google&logoColor=white)
![Kakao](https://img.shields.io/badge/kakao-ffcd00.svg?style=for-the-badge&logo=kakao&logoColor=000000)
![Naver](https://img.shields.io/badge/naver-00C300?style=for-the-badge&logo=naver&logoColor=white)

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
        <td><center><img align="top" src="https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white" alt="Github Actions"></center></td>
        <td valign="">Swagger</td>
        <td valign="">API 명세서 관리 자동화</td>
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

<br><br>

## 🏗 시스템 구조 (System Architecture)
### 전체 시스템 개요 (System Overview)
![시스템 구성](https://i.imgur.com/3xstw23.png)

### 소프트웨어 아키텍쳐 (Software Architecture)
![소프트웨어 아키텍쳐](https://i.imgur.com/OhYg2cD.png)

<br><br>

## :running: 주요 기능 시연 (Feature Demostration)
AJOU SOFTCON 홈페이지의 [살롱 소개 페이지](https://softcon.ajou.ac.kr/works/works.asp?uid=1784)를 참조해주세요.  

<br><br>

## 👥 팀원 구성 (Team Member List)

|  이름  |      학과      |    담당    | 이메일 | 비고 |
| :----: | :------------: | :--------: | :--: | --- |
| [신윤석](https://github.com/sys7498) | 소프트웨어학과 | 프론트엔드 | sys7498@ajou.ac.kr | 팀장 |
| [김세현](https://github.com/NSRBSG) | 소프트웨어학과 | 프론트엔드 |  -  |  |
| [김지환](https://github.com/jihwankim129) | 소프트웨어학과 |   백엔드   |  -  |  |
| [양성호](https://github.com/SyingSHY) | 소프트웨어학과 |   백엔드   |  -  |  |
| [이정준](https://github.com/lee1684) | 소프트웨어학과 |   백엔드   |  -  |  |

+ 팀원 이름 클릭 시 해당 팀원의 Github 프로필로 연결됩니다.

<br><br>

## 🎊 2024-1 AJOU SOFTCON 전시 (2024-1 AJOU SOFTCON Presentation)
[2024-1 AJOU SOFTCON](https://softcon.ajou.ac.kr/works/works.asp?uid=1784) : 이 링크를 클릭하여 SOFTCON 홈페이지 내 '살롱' 소개 페이지로 이동할 수 있습니다.

### 전시 포스터 (Presentation Poster)
![SSALON AJOU SOFTCON 포스터](https://i.imgur.com/yF4g8hi.png)

### 전시 소개 영상 (Presentation Video)
AJOU SOFTCON 홈페이지의 [살롱 소개 페이지](https://softcon.ajou.ac.kr/works/works.asp?uid=1784)를 참조해주세요.  

<br><br>

## 📜 라이선스 (License)
라이선스 내용입니다.
