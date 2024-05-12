import { Component } from '@angular/core';
import { TopNavigatorComponent } from '../ssalon-component/top-navigator/top-navigator.component';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-meeting-info',
  standalone: true,
  imports: [TopNavigatorComponent],
  templateUrl: './meeting-info.component.html',
  styleUrl: './meeting-info.component.scss',
})
export class MeetingInfoComponent {
  private _moimId: string = '';
  constructor(private _route: ActivatedRoute) {
    this._route.queryParams.subscribe((params) => {
      this._moimId = params['moimId'];
    });
  }
}
