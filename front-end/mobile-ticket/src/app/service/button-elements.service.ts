import { Injectable } from '@angular/core';
import { NewButtonElement } from '../ssalon-component/simple-toggle-group/simple-toggle-group.component';

@Injectable({
  providedIn: 'root',
})
export class ButtonElementsService {
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
  public interestSelectionButtons = [
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

  public categorySelectionButtons: NewButtonElement[] = [
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
  constructor() {}
  public getLabelByValue(buttonElements: NewButtonElement[], value: number) {
    return buttonElements.find((element) => element.value === value)?.label;
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
