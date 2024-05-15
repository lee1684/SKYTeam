import { Component } from '@angular/core';
import { SquareButtonComponent } from '../ssalon-component/square-button/square-button.component';
import {
  NewButtonElement,
  SimpleToggleGroupComponent,
} from '../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { Router } from '@angular/router';
import { ButtonElementsService } from '../service/button-elements.service';

@Component({
  selector: 'app-ssalon-login',
  standalone: true,
  imports: [SimpleToggleGroupComponent],
  templateUrl: './ssalon-login.component.html',
  styleUrl: './ssalon-login.component.scss',
})
export class SsalonLoginComponent {
  constructor(
    private _router: Router,
    public buttonElementsService: ButtonElementsService
  ) {}
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
