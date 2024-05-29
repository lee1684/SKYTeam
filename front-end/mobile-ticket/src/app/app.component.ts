import { Component } from '@angular/core';
import { ActivatedRoute, RouterOutlet } from '@angular/router';
import { TicketComponent } from './ticket/ticket.component';
import { SsalonConfigService } from './service/ssalon-config.service';
import { CommonModule } from '@angular/common';
import { OnboardingComponent } from './onboarding/onboarding.component';
import { MainComponent } from './main/main.component';
import { ApiExecutorService } from './service/api-executor.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    TicketComponent,
    CommonModule,
    OnboardingComponent,
    MainComponent,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  constructor(
    private _route: ActivatedRoute,
    private _ssalonConfigService: SsalonConfigService,
    private _apiExecutorService: ApiExecutorService
  ) {}

  public ngOnInit(): void {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${'access'}=`);
    this._apiExecutorService.setToken(parts.pop()!.split(';').shift()!);
    //this._apiExecutorService.setToken(
    //  'eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsInVzZXJuYW1lIjoia2FrYW8gMzQ1NzYwNDk5MCIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MTY5NjEwOTcsImV4cCI6MTcxNzA0NzQ5N30.XrBYS0WGW-OBlQBS8xdHhh7UqERx2ERVTdywAFlv_zg'
    //);

    this._apiExecutorService.getMyProfile();
  }
}
