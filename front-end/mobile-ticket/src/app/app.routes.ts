import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { OnboardingComponent } from './onboarding/onboarding.component';
import { MainComponent } from './main/main.component';
import { TicketComponent } from './ticket/ticket.component';
import { MeetingInfoComponent } from './meeting-info/meeting-info.component';
import { SsalonLoginComponent } from './ssalon-login/ssalon-login.component';
import { SsalonLoginRedirectComponent } from './ssalon-login-redirect/ssalon-login-redirect.component';
import { MeetingCreateComponent } from './meeting-create/meeting-create.component';
import { MoimReviewComponent } from './meeting-info/moim-review/moim-review.component';
import { MoimReviewCreateComponent } from './meeting-info/moim-review/moim-review-create/moim-review-create.component';

export const routes: Routes = [
  { path: 'web/ssalon-login', component: SsalonLoginComponent },
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
    path: 'web/meeting-create',
    component: MeetingCreateComponent,
  },
  {
    path: 'web/ticket',
    component: TicketComponent,
  },
  {
    path: 'web/meeting-review-create',
    component: MoimReviewCreateComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
