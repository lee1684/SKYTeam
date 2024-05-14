import { Component } from '@angular/core';
import { SquareButtonComponent } from '../ssalon-component/square-button/square-button.component';
import {
  NewButtonElement,
  SimpleToggleGroupComponent,
} from '../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-ssalon-login',
  standalone: true,
  imports: [SimpleToggleGroupComponent],
  templateUrl: './ssalon-login.component.html',
  styleUrl: './ssalon-login.component.scss',
})
export class SsalonLoginComponent {
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
      backgroundColor: '#FEE500',
      fontColor: '#000000',
      imgSrc: 'assets/login-icons/kakao.png',
    },
    {
      selected: false,
      value: 2,
      label: 'Naver로 시작하기',
      solid: true,
      backgroundColor: '#03C75A',
      fontColor: '#ffffff',
      imgSrc: 'assets/login-icons/naver.png',
    },
  ];
  constructor(private _router: Router) {}
  public onClickLoginButton(value: number) {
    let redirectUrl: string;
    if (value === 0) {
      redirectUrl = 'https://ssalon.co.kr/oauth2/authorization/google';
    } else if (value === 1) {
      redirectUrl = 'https://ssalon.co.kr/oauth2/authorization/kakao';
    } else {
      redirectUrl = 'https://ssalon.co.kr/oauth2/authorization/naver';
    }
    window.location.href = redirectUrl;
  }
}
