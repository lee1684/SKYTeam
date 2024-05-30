import { Component } from '@angular/core';
import { TicketComponent } from '../ticket/ticket.component';
import { ApiExecutorService } from '../service/api-executor.service';
import { ActivatedRoute } from '@angular/router';
import { SimpleButtonComponent } from '../ssalon-component/simple-button/simple-button.component';
import { ButtonElementsService } from '../service/button-elements.service';
import { SquareButtonComponent } from '../ssalon-component/square-button/square-button.component';

@Component({
  selector: 'app-meeting-share',
  standalone: true,
  imports: [TicketComponent, SquareButtonComponent],
  templateUrl: './meeting-share.component.html',
  styleUrl: './meeting-share.component.scss',
})
export class MeetingShareComponent {
  public moimId: string = '';
  constructor(
    private _apiExecutorService: ApiExecutorService,
    private _route: ActivatedRoute,
    public buttonElementsService: ButtonElementsService
  ) {}
  public ngOnInit(): void {
    this._route.queryParams.subscribe((params) => {
      this.moimId = params['i'];
    });
  }
}
