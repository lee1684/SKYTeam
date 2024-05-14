import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { OnboardingComponent } from './onboarding/onboarding.component';
import { MainComponent } from './main/main.component';
import { TicketComponent } from './ticket/ticket.component';
import { MeetingInfoComponent } from './meeting-info/meeting-info.component';
import { SsalonLoginComponent } from './ssalon-login/ssalon-login.component';
import { SsalonLoginRedirectComponent } from './ssalon-login-redirect/ssalon-login-redirect.component';

export const routes: Routes = [
  { path: 'web/ssalon-login', component: SsalonLoginComponent },
  { path: 'login/oauth2/code/google', component: SsalonLoginRedirectComponent },
  { path: 'login/oauth2/code/kakao', component: SsalonLoginRedirectComponent },
  { path: 'login/oauth2/code/naver', component: SsalonLoginRedirectComponent },
  {
    path: 'web/ssalon-login-redirect',
    component: SsalonLoginRedirectComponent,
  },
  {
    path: 'web/onboarding',
    component: OnboardingComponent,
  },
  {
    path: 'web/main',
    component: MainComponent,
  },
  {
    path: 'web/meeting-info',
    component: MeetingInfoComponent,
  },
  {
    path: 'web/ticket',
    component: TicketComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
