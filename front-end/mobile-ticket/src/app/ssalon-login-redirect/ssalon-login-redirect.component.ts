import { Component } from '@angular/core';
import { ApiExecutorService } from '../service/api-executor.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-ssalon-login-redirect',
  standalone: true,
  imports: [],
  templateUrl: './ssalon-login-redirect.component.html',
  styleUrl: './ssalon-login-redirect.component.scss',
})
export class SsalonLoginRedirectComponent {
  public ts = '';
  constructor(
    private _apiExecutorService: ApiExecutorService,
    private _router: Router
  ) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${'access'}=`);
    this._apiExecutorService.setToken(parts.pop()!.split(';').shift()!);
  }
  public async ngOnInit() {
    let response = await this._apiExecutorService.getIsRegister();
    console.log(response);
    setTimeout(() => {}, 3000);

    if (response !== false) {
      this._router.navigate(['/web/main']);
    } else {
      this._router.navigate(['/web/onboarding']);
    }
  }
}
