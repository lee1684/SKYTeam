import { Component } from '@angular/core';
import { ActivatedRoute, RouterOutlet } from '@angular/router';
import { TicketComponent } from './ticket/ticket.component';
import { SsalonConfigService } from './service/ssalon-config.service';
import { CommonModule } from '@angular/common';
import { OnboardingComponent } from './onboarding/onboarding.component';
import { MainComponent } from './main/main.component';

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
    private _ssalonConfigService: SsalonConfigService
  ) {}
}
