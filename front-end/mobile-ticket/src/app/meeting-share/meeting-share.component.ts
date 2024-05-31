import { Component } from '@angular/core';
import { TicketComponent } from '../ticket/ticket.component';
import { ApiExecutorService } from '../service/api-executor.service';
import { ActivatedRoute, Router } from '@angular/router';
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
    private _router: Router,
    public buttonElementsService: ButtonElementsService
  ) {
    this._route.queryParams.subscribe((params) => {
      this.moimId = params['id'];
      console.log(params);
    });
  }
  public ngOnInit(): void {}
  public joinMoim() {
    this._router.navigate(['/web/meeting-info'], {
      queryParams: { moimId: this.moimId },
    });
  }
}
