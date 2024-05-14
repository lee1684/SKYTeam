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
  constructor(
    private _apiExecutorService: ApiExecutorService,
    private _router: Router
  ) {}
  public ngOnInit() {
    let value = '; ' + document.cookie;
    let parts = value.split('; ' + 'token' + '=');
    console.log(document.cookie, parts);
    console.log('ASDFASDFASFDSa');
    this._router.navigate(['/onboarding']);
  }
}
