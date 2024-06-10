import { Component } from '@angular/core';
import { SquareButtonComponent } from '../ssalon-component/square-button/square-button.component';
import {
  NewButtonElement,
  SimpleToggleGroupComponent,
} from '../ssalon-component/simple-toggle-group/simple-toggle-group.component';
<<<<<<< HEAD
import { ActivatedRoute, Router } from '@angular/router';
import { ButtonElementsService } from '../service/button-elements.service';
import { TicketComponent } from '../ticket/ticket.component';
import { NgIf } from '@angular/common';
=======
import { Router } from '@angular/router';
import { ButtonElementsService } from '../service/button-elements.service';
import { TicketComponent } from '../ticket/ticket.component';
>>>>>>> develop

@Component({
  selector: 'app-ssalon-login',
  standalone: true,
<<<<<<< HEAD
  imports: [SimpleToggleGroupComponent, TicketComponent, NgIf],
=======
  imports: [SimpleToggleGroupComponent, TicketComponent],
>>>>>>> develop
  templateUrl: './ssalon-login.component.html',
  styleUrl: './ssalon-login.component.scss',
})
export class SsalonLoginComponent {
  public defaultUrl: string = 'https://ssalon.co.kr/oauth2/authorization';
  //public defaultUrl: string = 'http://localhost:8080/oauth2/authorization';
<<<<<<< HEAD
  public goMoimId: string = '';
  constructor(
    private _router: Router,
    private _route: ActivatedRoute,
    public buttonElementsService: ButtonElementsService
  ) {
    this._route.queryParams.subscribe((params) => {
      this.goMoimId = params['moimId'];
      sessionStorage.setItem('goMoimId', this.goMoimId);
    });
  }
  public onClickLoginButton(value: number) {
    console.log(this.goMoimId);
    let redirectUrl: string;
    if (value === 0) {
      redirectUrl = `${this.defaultUrl}/google`;
    } else if (value === 1) {
      redirectUrl = `${this.defaultUrl}/kakao`;
    } else {
      redirectUrl = `${this.defaultUrl}/naver`;
    }
    window.location.href = redirectUrl;
  }
  public isLowerWidth(): boolean {
    return window.innerWidth < 800;
  }
=======
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
>>>>>>> develop
}
