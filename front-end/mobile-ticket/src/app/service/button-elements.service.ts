import { Injectable } from '@angular/core';
import { NewButtonElement } from '../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { ApiExecutorService } from './api-executor.service';

@Injectable({
  providedIn: 'root',
})
export class ButtonElementsService {
  private mainCategoryTab: string[] = [
    '추천',
    '운동',
    '독서',
    '요리',
    '여행',
    '음악',
    '스터디',
    '쇼핑',
    '예술',
    '사진',
    '게임',
  ];
  private category: string[] = [
    '운동',
    '독서',
    '요리',
    '여행',
    '음악',
    '스터디',
    '쇼핑',
    '예술',
    '사진',
    '게임',
  ];

  private categoryEnglish: string[] = [
    'exercise',
    'book',
    'food',
    'travel',
    'music',
    'study',
    'shopping',
    'art',
    'photography',
    'game',
  ];

  /**
   * Meeting Info
   */

  public joinButtonElements: NewButtonElement[] = [
    {
      selected: true,
      value: 0,
      label: '참가하기',
    },
  ];

  public createDiaryButtonElements: NewButtonElement[] = [
    {
      selected: true,
      value: 0,
      label: '후기 작성하기',
    },
  ];

  /**
   * Login
   */

  public loginButtonElements: NewButtonElement[] = [
    {
      selected: false,
      value: 0,
      label: 'Google로 시작하기',
      solid: false,
      imgSrc: 'assets/login-icons/google.png',
    },
    {
      selected: false,
      value: 1,
      label: 'Kakao로 시작하기',
      solid: true,
      unselectedBackgroundColor: '#FEE500',
      unselectedFontColor: '#000000',
      selectedBackgroundColor: '#FEE500',
      selectedFontColor: '#000000',
      imgSrc: 'assets/login-icons/kakao.png',
    },
    {
      selected: false,
      value: 2,
      label: 'Naver로 시작하기',
      solid: true,
      unselectedBackgroundColor: '#03C75A',
      unselectedFontColor: '#ffffff',
      selectedBackgroundColor: '#03C75A',
      selectedFontColor: '#ffffff',
      imgSrc: 'assets/login-icons/naver.png',
    },
  ];

  /**
   * Onboarding
   */

  /** 첫번째 화면 */
  public genderSelectionButtons = [
    {
      selected: false,
      value: 0,
      label: '남자',
    },
    { selected: false, value: 1, label: '여자' },
    { selected: false, value: 2, label: '기타' },
  ];
  /** 두번째 화면 */
  public locationSelectionButtons: NewButtonElement[] = [
    { selected: false, value: 0, label: '서울특별시' },
    { selected: false, value: 1, label: '경기도' },
    { selected: false, value: 2, label: '강원도' },
    { selected: false, value: 3, label: '충청북도' },
    { selected: false, value: 4, label: '충청남도' },
    { selected: false, value: 5, label: '전라남도' },
    { selected: false, value: 6, label: '전라북도' },
    { selected: false, value: 7, label: '경상북도' },
    { selected: false, value: 8, label: '경상남도' },
    { selected: false, value: 9, label: '인천광역시' },
  ];
  /** 세번째 화면 */
  public interestSelectionButtons: NewButtonElement[] = [];

  /**
   * Main
   */

  public categorySelectionButtons: NewButtonElement[] = [];

  /**
   * Meeting Create
   */

  public createTicketButtons: NewButtonElement[] = [
    {
      selected: false,
      value: 0,
      label: '티켓만들기',
    },
  ];
  public createTicketTypeButtons: NewButtonElement[] = [
    {
      selected: false,
      value: 0,
      label: '처음부터 만들기',
      imgSrc: 'assets/create-ticket-type-icons/create.png',
    },
    {
      selected: false,
      value: 1,
      label: '템플릿으로 만들기',
      imgSrc: 'assets/create-ticket-type-icons/template.png',
    },
  ];
  public nextButtons: NewButtonElement[] = [
    {
      selected: false,
      value: 0,
      label: '다음',
    },
  ];
  public shareJoinButtons: NewButtonElement[] = [
    {
      selected: true,
      value: 0,
      label: '모임 참여해보기',
    },
  ];

  public editCompleteButtons: NewButtonElement[] = [
    {
      selected: true,
      value: 0,
      label: '수정 완료',
    },
  ];

  public editTicketButtons: NewButtonElement[] = [
    {
      selected: false,
      value: 0,
      label: '증표 앞면 수정하기',
    },
    {
      selected: false,
      value: 1,
      label: '증표 뒷면 수정하기',
    },
  ];
  public editProfileButton: NewButtonElement[] = [
    {
      selected: false,
      value: 0,
      label: '프로필 수정하기',
    },
    {
      selected: false,
      value: 1,
      label: '로그아웃',
    },
  ];
  public manageParticipantsButtons: NewButtonElement[] = [
    {
      selected: false,
      value: 0,
      label: '신고하기',
    },
    {
      selected: false,
      value: 1,
      label: '강퇴하기',
    },
  ];

  public removeAccountButtons: NewButtonElement[] = [
    {
      selected: true,
      value: 0,
      label: '취소하기',
    },
    {
      selected: false,
      value: 1,
      label: '탈퇴하기',
    },
  ];
  constructor(private _apiExecutorService: ApiExecutorService) {
    for (let i = 0; i < 10; i++) {
      this.interestSelectionButtons.push({
        selected: false,
        value: i,
        label: this.category[i],
        imgSrc: `assets/interest-icons/${this.categoryEnglish[i]}.png`,
      });
    }

    for (let i = 0; i < 11; i++) {
      this.categorySelectionButtons.push({
        selected: i === 0, // 0번 버튼은 선택된 상태로 초기화
        value: i,
        label: this.mainCategoryTab[i],
        solid: true,
        unselectedBackgroundColor: '#F8F8F8',
        unselectedFontColor: '#000000',
        selectedBackgroundColor: '#000000',
        selectedFontColor: '#ffffff',
      });
    }
  }

  public async updateCategoryOrder(): Promise<boolean> {
    this.categorySelectionButtons = [];
    let orderedCateogrySelectionButtons =
      await this._apiExecutorService.getRecommendedCategorys();
    orderedCateogrySelectionButtons.forEach(
      (category: { id: number; name: string }) => {
        this.categorySelectionButtons.push({
          selected: false, // 0번 버튼은 선택된 상태로 초기화
          value: category.id,
          label: this.category[category.id - 1],
          solid: true,
          unselectedBackgroundColor: '#F8F8F8',
          unselectedFontColor: '#000000',
          selectedBackgroundColor: '#000000',
          selectedFontColor: '#ffffff',
        });
      }
    );
    this.categorySelectionButtons.unshift({
      selected: true,
      value: 0,
      label: '추천',
      solid: true,
      unselectedBackgroundColor: '#F8F8F8',
      unselectedFontColor: '#000000',
      selectedBackgroundColor: '#000000',
      selectedFontColor: '#ffffff',
    });
    return true;
  }

  public getLabelByValue(buttonElements: NewButtonElement[], value: number) {
    return buttonElements.find((element) => element.value === value)?.label;
  }

  public getValueByLabel(buttonElements: NewButtonElement[], label: string) {
    return buttonElements.find((element) => element.label === label)?.value;
  }
  public inintButtons() {
    /**
     * Meeting Info
     */

    this.joinButtonElements = [
      {
        selected: true,
        value: 0,
        label: '참가하기',
      },
    ];

    /**
     * Login
     */

    this.loginButtonElements = [
      {
        selected: false,
        value: 0,
        label: 'Google로 시작하기',
        solid: false,
        imgSrc: 'assets/login-icons/google.png',
      },
      {
        selected: false,
        value: 1,
        label: 'Kakao로 시작하기',
        solid: true,
        unselectedBackgroundColor: '#FEE500',
        unselectedFontColor: '#000000',
        selectedBackgroundColor: '#FEE500',
        selectedFontColor: '#000000',
        imgSrc: 'assets/login-icons/kakao.png',
      },
      {
        selected: false,
        value: 2,
        label: 'Naver로 시작하기',
        solid: true,
        unselectedBackgroundColor: '#03C75A',
        unselectedFontColor: '#ffffff',
        selectedBackgroundColor: '#03C75A',
        selectedFontColor: '#ffffff',
        imgSrc: 'assets/login-icons/naver.png',
      },
    ];

    /**
     * Onboarding
     */

    /** 첫번째 화면 */
    this.genderSelectionButtons = [
      {
        selected: false,
        value: 0,
        label: '남자',
      },
      { selected: false, value: 1, label: '여자' },
      { selected: false, value: 2, label: '기타' },
    ];
    /** 두번째 화면 */
    this.locationSelectionButtons = [
      { selected: false, value: 0, label: '서울특별시' },
      { selected: false, value: 1, label: '경기도' },
      { selected: false, value: 2, label: '강원도' },
      { selected: false, value: 3, label: '충청북도' },
      { selected: false, value: 4, label: '충청남도' },
      { selected: false, value: 5, label: '전라남도' },
      { selected: false, value: 6, label: '전라북도' },
      { selected: false, value: 7, label: '경상북도' },
      { selected: false, value: 8, label: '경상남도' },
      { selected: false, value: 9, label: '인천광역시' },
    ];
    /** 세번째 화면 */
    this.interestSelectionButtons = [
      {
        selected: false,
        value: 0,
        label: '운동',
        imgSrc: 'assets/interest-icons/excersize.png',
      },
      {
        selected: false,
        value: 1,
        label: '게임',
        imgSrc: 'assets/interest-icons/game.png',
      },
      {
        selected: false,
        value: 2,
        label: '음악',
        imgSrc: 'assets/interest-icons/music.png',
      },
      {
        selected: false,
        value: 3,
        label: '요리',
        imgSrc: 'assets/interest-icons/food.png',
      },
      {
        selected: false,
        value: 4,
        label: '독서',
        imgSrc: 'assets/interest-icons/book.png',
      },
    ];

    /**
     * Main
     */

    this.categorySelectionButtons = [
      {
        selected: true,
        value: 0,
        label: '전체',
        solid: true,
        unselectedBackgroundColor: '#F8F8F8',
        unselectedFontColor: '#000000',
        selectedBackgroundColor: '#000000',
        selectedFontColor: '#ffffff',
      },
      {
        selected: false,
        value: 1,
        label: '운동',
        solid: true,
        unselectedBackgroundColor: '#F8F8F8',
        unselectedFontColor: '#000000',
        selectedBackgroundColor: '#000000',
        selectedFontColor: '#ffffff',
      },
      {
        selected: false,
        value: 2,
        label: '게임',
        solid: true,
        unselectedBackgroundColor: '#F8F8F8',
        unselectedFontColor: '#000000',
        selectedBackgroundColor: '#000000',
        selectedFontColor: '#ffffff',
      },
      {
        selected: false,
        value: 3,
        label: '음악',
        solid: true,
        unselectedBackgroundColor: '#F8F8F8',
        unselectedFontColor: '#000000',
        selectedBackgroundColor: '#000000',
        selectedFontColor: '#ffffff',
      },
      {
        selected: false,
        value: 4,
        label: '요리',
        solid: true,
        unselectedBackgroundColor: '#F8F8F8',
        unselectedFontColor: '#000000',
        selectedBackgroundColor: '#000000',
        selectedFontColor: '#ffffff',
      },
      {
        selected: false,
        value: 5,
        label: '독서',
        solid: true,
        unselectedBackgroundColor: '#F8F8F8',
        unselectedFontColor: '#000000',
        selectedBackgroundColor: '#000000',
        selectedFontColor: '#ffffff',
      },
      {
        selected: false,
        value: 6,
        label: '영화',
        solid: true,
        unselectedBackgroundColor: '#F8F8F8',
        unselectedFontColor: '#000000',
        selectedBackgroundColor: '#000000',
        selectedFontColor: '#ffffff',
      },
    ];

    /**
     * Meeting Create
     */

    this.createTicketButtons = [
      {
        selected: false,
        value: 0,
        label: '티켓만들기',
      },
    ];
    this.createTicketTypeButtons = [
      {
        selected: false,
        value: 0,
        label: '처음부터 만들기',
        imgSrc: 'assets/create-ticket-type-icons/create.png',
      },
      {
        selected: false,
        value: 1,
        label: '템플릿으로 만들기',
        imgSrc: 'assets/create-ticket-type-icons/template.png',
      },
    ];
    this.nextButtons = [
      {
        selected: false,
        value: 0,
        label: '다음',
      },
    ];
  }
}
