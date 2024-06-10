<div align="center">
    
# :dancers: SSALON: 증표 기반 일회성 모임관리 서비스
![SSALON 메인 이미지](https://i.imgur.com/OVJeDPd.png)  
:house: **서비스 홈페이지** : [살롱 바로가기](https://ssalon.co.kr)  
:open_file_folder: **레포지토리 루트로 이동** : [레포지토리 루트](/)  
:open_file_folder: **각 엔드별 상세 레포지토리** : [프론트엔드](/front-end) | [백엔드](/back-end)  
:key: **API Specification** : [Swagger](https://ssalon.co.kr/swagger-ui/index.html#/)  

</div> <br><br><br><br>

## 🔍 백엔드 개요 (Back-end Overview)
최근 사람들은 새로운 곳에 소속되어 새로운 관계가 생기는 것을 꺼려하는 등 집단주의 보다는 개인주의적 성향이 매우 강한 편입니다. 이러한 배경에서 일회성 모임에 대한 수요는 계속 해서 늘어나고 있는 상황입니다. 하지만 참가자 인증의 불편함, 모임 홍보의 어려움, 추가적인 추억 보존 수단 수요, 불만족스러운 서비스 이용자 경험 등으로 인해 현재 시장에 나와있는 서비스들은 소비자들의 수요를 충분히 만족시키지 못하고 있다고 판단됩니다.  

위의 문제를 해결하기 위한 <strong>SSALON'(이하 '살롱')</strong>은 '인터랙티브 3D 증표' 기반의 일회성 모임 커뮤니티를 제공하는 웹 서비스입니다. 사용자는 증표를 바탕으로 모임의 개최, 참가, 관리, 추억, 인증 등 일회성 모임과 관련된 모든 활동을 편리하게 이용할 수 있습니다. 특히, 증표를 이용한 추억 기록은 타 서비스와 비교되는 특장점(POD)으로 '살롱' 만의 '3D 증표'를 이용하여 보다 쉽고, 재미있는 일회성 모임 경험을 제공받을 수 있을 것입니다.  

이 프로젝트는 2024년 1학기 아주대학교 소프트웨어학과 SW캡스톤디자인 수업의 일환으로 11팀 SKYTeam 팀이 2024년 3월부터 6월까지 개발 진행하였습니다.  

<br><br>

## 🔧 주요 기술스택 (Technology Stacks)
### 백엔드 (Back-end)
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
        <td valign="">임베딩 이용 추천 시스템 관련 로직 구현</td>
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
        <td valign="">사용자, 모임, 카테고리 정보 임베딩</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/google-4285F4?style=for-the-badge&logo=google&logoColor=white" alt="Google"></center></td>
        <td valign="">Google</td>
        <td valign="">OAuth 사용자 인증</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/kakao-ffcd00.svg?style=for-the-badge&logo=kakao&logoColor=000000" alt="Kakao"></center></td>
        <td valign="">Kakao</td>
        <td valign="">OAuth 사용자 인증 / 카카오페이 결제 처리 / Karlo 이미지 생성 AI</td>
    </tr>
    <tr>
        <td><center><img align="top" src="https://img.shields.io/badge/naver-00C300?style=for-the-badge&logo=naver&logoColor=white" alt="Naver"></center></td>
        <td valign="">Naver</td>
        <td valign="">OAuth 사용자 인증</td>
    </tr>
</table>

<br><br>

## 👥 팀원 구성 (Team Member List)

|  이름  |      학과      |    담당    | 이메일 | 비고 |
| :----: | :------------: | :--------: | :--: | --- |
| [김지환](https://github.com/jihwankim129) | 소프트웨어학과 |   백엔드   |  -  |  |
| [양성호](https://github.com/SyingSHY) | 소프트웨어학과 |   백엔드   |  -  |  |
| [이정준](https://github.com/lee1684) | 소프트웨어학과 |   백엔드   |  -  |  |

+ 팀원 이름 클릭 시 해당 팀원의 Github 프로필로 연결됩니다.
