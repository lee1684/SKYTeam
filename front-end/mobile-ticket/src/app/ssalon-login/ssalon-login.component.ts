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
  public defaultUrl: string = 'https://ssalon.co.kr/oauth2/authorization';
  //public defaultUrl: string = 'http://localhost:8080/oauth2/authorization';
  constructor(
    private _router: Router,
    public buttonElementsService: ButtonElementsService
  ) {}
  public onClickLoginButton(value: number) {
    let redirectUrl: string;
    if (value === 0) {
      redirectUrl = this.defaultUrl + '/google';
    } else if (value === 1) {
      redirectUrl = this.defaultUrl + '/kakao';
    } else {
      redirectUrl = this.defaultUrl + '/naver';
    }
    window.location.href = redirectUrl;
  }
}
