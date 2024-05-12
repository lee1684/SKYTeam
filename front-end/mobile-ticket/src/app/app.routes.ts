import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { ResolverService } from './service/resolver.service';
import { NgModule } from '@angular/core';
import { OnboardingComponent } from './onboarding/onboarding.component';
import { MainComponent } from './main/main.component';
import { TicketComponent } from './ticket/ticket.component';
import { MeetingInfoComponent } from './meeting-info/meeting-info.component';

export const routes: Routes = [
  {
    path: 'onboarding',
    component: OnboardingComponent,
  },
  {
    path: 'main',
    component: MainComponent,
  },
  {
    path: 'meeting-info',
    component: MeetingInfoComponent,
  },
  {
    path: 'ticket',
    component: TicketComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
