import { Routes } from '@angular/router';
import { AppComponent } from './app.component';

export const routes: Routes = [
  {
    path: 'info?moim=:moimId&page=:viewType&face=:faceType',
    component: AppComponent,
  },
];
// 페이지타입: enum { view=0, edit=1, share=2}
// 앞또는뒤: enum { front=0, back=1, null=2 } null은 페이지타입 share에서 사용
// /info?moim={모임아이디}&page={페이지타입}&face={앞또는뒤}
// ex) /info?moim=11111111&page=1&face=0
