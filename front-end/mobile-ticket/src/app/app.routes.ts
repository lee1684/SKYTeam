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
import { MeetingShareComponent } from './meeting-share/meeting-share.component';
import { MeetingEditComponent } from './meeting-edit/meeting-edit.component';
import { ProfileUpdateComponent } from './main/profile/profile-update/profile-update.component';

export const routes: Routes = [
  {
    path: '',
    component: SsalonLoginComponent,
  },
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
  {
    path: 'web/share',
    component: MeetingShareComponent,
  },
  {
    path: 'web/meeting-edit',
    component: MeetingEditComponent,
  },
  {
    path: 'web/profile-update',
    component: ProfileUpdateComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
