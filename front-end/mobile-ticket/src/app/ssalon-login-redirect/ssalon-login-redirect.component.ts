import { Component } from '@angular/core';
import { ApiExecutorService } from '../service/api-executor.service';
<<<<<<< HEAD
import { ActivatedRoute, Router } from '@angular/router';
=======
import { Router } from '@angular/router';
>>>>>>> develop

@Component({
  selector: 'app-ssalon-login-redirect',
  standalone: true,
  imports: [],
  templateUrl: './ssalon-login-redirect.component.html',
  styleUrl: './ssalon-login-redirect.component.scss',
})
export class SsalonLoginRedirectComponent {
  public ts = '';
<<<<<<< HEAD
  private goMoimId = undefined as unknown as string;
  constructor(
    private _apiExecutorService: ApiExecutorService,
    private _router: Router,
    private _route: ActivatedRoute
  ) {
    if (sessionStorage.getItem('goMoimId')) {
      this.goMoimId = sessionStorage.getItem('goMoimId')!;
    } else {
      this.goMoimId = 'undefined';
    }
=======
  constructor(
    private _apiExecutorService: ApiExecutorService,
    private _router: Router
  ) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${'access'}=`);
    this._apiExecutorService.setToken(parts.pop()!.split(';').shift()!);
>>>>>>> develop
  }
  public async ngOnInit() {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${'access'}=`);
<<<<<<< HEAD
    this._apiExecutorService.setToken();
    let response = await this._apiExecutorService.getIsRegister();

    if (response !== false) {
      if (this.goMoimId === 'undefined') {
        this._router.navigate(['/web/main']);
      } else {
        this._router.navigate(['/web/meeting-info'], {
          queryParams: { moimId: this.goMoimId },
        });
      }
=======
    this._apiExecutorService.setToken(parts.pop()!.split(';').shift()!);
    let response = await this._apiExecutorService.getIsRegister();
    console.log(response);
    setTimeout(() => {}, 3000);

    if (response !== false) {
      this._router.navigate(['/web/main']);
>>>>>>> develop
    } else {
      this._router.navigate(['/web/onboarding']);
    }
  }
}
