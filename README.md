# :dancers: SSALON: 증표 기반 일회성 모임관리 서비스
![SSALON 메인 이미지](https://i.imgur.com/OVJeDPd.png)

## 🔍 프로젝트 개요 (Overview)
**SSANLON(이하 '살롱')**은 증표 기반 일회성 모임 관리 웹 서비스입니다. '살롱'은 ... 제공합니다.

이 프로젝트는 2024년 1학기 아주대학교 소프트웨어학과 SW캡스톤디자인 수업의 일환으로 11팀 SKYTeam 팀이 2024년 3월부터 6월까지 개발 진행하였습니다. 

## 🪄 프로젝트 제안 (Project Introduction)
### 문제 정의 (Problems We Found)
...

### 살롱이 제안하는 해결책 (Solutions We Suggest)
...

### 타겟과 이해관계자 (Targets & Stakeholders)
...

## 🌠 프로젝트 주요 기능 (Key Features)
### 증표
...
### 추천 시스템
...
### 채팅
...
### 와우 이거 정말 좋은데여???
...

## 🔧 주요 기술스택 (Technology Stacks)
### 프론트엔드 (Front-End)
<table>
    <tr>
        <th><center>-</center></th>
        <th align="center"><center>이름</center></th>
        <th>사용 용도</th>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white" alt="Slack"></center></td>
        <td valign="">Slack</td>
        <td valign="">Github Push 알림 / 기술 정보 공유 및 관리</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/kakaotalk-ffcd00.svg?style=for-the-badge&logo=kakaotalk&logoColor=black" alt="Slack"></center></td>
        <td valign="">KakaoTalk</td>
        <td valign="">팀원 간 소통 및 긴급 공지</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/kakaotalk-ffcd00.svg?style=for-the-badge&logo=kakaotalk&logoColor=black" alt="Slack"></center></td>
        <td valign="">Google</td>
        <td valign="">팀원 간 소통 및 긴급 공지</td>
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
        <td valign="">Spring Boot 로직 작성 시 사용한 주요 프로그래밍 언어</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/python-3670A0?style=for-the-badge&logo=python&logoColor=ffdd54" alt="Python"></center></td>
        <td valign="">Python</td>
        <td valign="">AWS Lambda 로직 작성 시 사용한 주요 프로그래밍 언어</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=Spring%20Boot&logoColor=white" alt="Spring Boot"></center></td>
        <td valign="">Spring Boot</td>
        <td valign="">서버 주요 로직 구현</td>
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
        <td valign="">추천 시스템의 벡터 DB로서 임베딩 정보 관리 및 벡터 유사도 검색 수행</td>
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
</table>

### 외부 API (External API)
<table>
    <tr>
        <th><center>-</center></th>
        <th align="center"><center>이름</center></th>
        <th>사용 용도</th>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white" alt="Slack"></center></td>
        <td valign="">Slack</td>
        <td valign="">Github Push 알림 / 기술 정보 공유 및 관리</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/kakaotalk-ffcd00.svg?style=for-the-badge&logo=kakaotalk&logoColor=black" alt="Slack"></center></td>
        <td valign="">KakaoTalk</td>
        <td valign="">팀원 간 소통 및 긴급 공지</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/kakaotalk-ffcd00.svg?style=for-the-badge&logo=kakaotalk&logoColor=black" alt="Slack"></center></td>
        <td valign="">Google</td>
        <td valign="">팀원 간 소통 및 긴급 공지</td>
    </tr>
</table>

### 데브옵스 (DevOps)
<table>
    <tr>
        <th><center>-</center></th>
        <th align="center"><center>이름</center></th>
        <th>사용 용도</th>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white" alt="Slack"></center></td>
        <td valign="">Slack</td>
        <td valign="">Github Push 알림 / 기술 정보 공유 및 관리</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/kakaotalk-ffcd00.svg?style=for-the-badge&logo=kakaotalk&logoColor=black" alt="Slack"></center></td>
        <td valign="">KakaoTalk</td>
        <td valign="">팀원 간 소통 및 긴급 공지</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/kakaotalk-ffcd00.svg?style=for-the-badge&logo=kakaotalk&logoColor=black" alt="Slack"></center></td>
        <td valign="">Google</td>
        <td valign="">팀원 간 소통 및 긴급 공지</td>
    </tr>
</table>

### 커뮤니케이션 도구 (Communication Tools)
<table>
    <tr>
        <th><center>-</center></th>
        <th align="center"><center>이름</center></th>
        <th>사용 용도</th>
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
        <td><center><img align="top" src="https://img.shields.io/badge/kakaotalk-ffcd00.svg?style=for-the-badge&logo=kakaotalk&logoColor=black" alt="Slack"></center></td>
        <td valign="">Google</td>
        <td valign="">팀원 간 소통 및 긴급 공지</td>
    </tr>
</table>

## 🏗 시스템 구조 (System Architecture)
### 전체 시스템 개요 (System Overview)
![시스템 구성](https://i.imgur.com/3xstw23.png)

### 소프트웨어 아키텍쳐 (Software Architecture)
총 이미지 2장 필요 : 구성요소(위에 올려놓음), 아키텍쳐

## :running: 주요 기능 시연 (Feature Demostration)
시연 영상 첨부

## 👥 팀원 구성 (Team Member List)

|  이름  |   학번    |      학과      |    담당    | 비고 |
| :----: | :-------: | :------------: | :--------: | :--: |
| [신윤석](https://github.com/sys7498) | 201821090 | 소프트웨어학과 | 프론트엔드 | 팀장 |
| [김세현](https://github.com/NSRBSG) | 201820789 | 소프트웨어학과 | 프론트엔드 |      |
| [김지환](https://github.com/jihwankim129) | 201920000 | 소프트웨어학과 |   백엔드   |      |
| [양성호](https://github.com/SyingSHY) | 201720721 | 소프트웨어학과 |   백엔드   |      |
| [이정준](https://github.com/lee1684) | 201820000 | 소프트웨어학과 |   백엔드   |      |

+ 팀원 이름 클릭 시 해당 팀원의 Github 프로필로 연결됩니다.
+ Special Thanks to 한수현 멘토님

## 🎊 2024-1 AJOU SOFTCON 전시 (2024-1 AJOU SOFTCON Presentation)
2024-1 AJOU SOFTCON : 이 링크를 클릭하여 SOFTCON 홈페이지 내 '살롱' 페이지로 이동할 수 있습니다.

### 전시 포스터 (Presentation Poster)
![SSALON AJOU SOFTCON 포스터](https://i.imgur.com/yF4g8hi.png)

### 전시 소개 영상 (Presentation Video)
영상 첨부 위치

## 📜 라이선스 (License)
라이선스 내용입니다.
