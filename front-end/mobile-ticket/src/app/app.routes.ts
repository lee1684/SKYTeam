import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { ResolverService } from './service/resolver.service';
import { NgModule } from '@angular/core';

export const routes: Routes = [
  {
    path: '?moimId=:moimId&viewType=:viewType&faceType=:faceType&qr=:qr',
    component: AppComponent,
  },
  {
    path: '',
    component: AppComponent,
    resolve: {
      data: ResolverService,
    },
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
