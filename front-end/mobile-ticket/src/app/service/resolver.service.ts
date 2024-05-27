import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot,
} from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ResolverService implements Resolve<any> {
  constructor() {}
  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<any> | Promise<any> | any {
    // URL에서 파라미터 값을 가져와서 객체로 반환합니다.
    return {
      moimId: route.queryParams['moimId'],
      viewType: route.queryParams['viewType'],
      faceType: route.queryParams['faceType'],
      qr: route.queryParams['qr'],
    };
  }
}
