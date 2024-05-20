import { Component } from '@angular/core';
import { TabGroupComponent } from '../../ssalon-component/tab-group/tab-group.component';
import { ButtonElementsService } from '../../service/button-elements.service';
import { NewButtonElement } from '../../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { Ticket } from '../../ssalon-component/image-row-container/image-row-container.component';
import { ApiExecutorService } from '../../service/api-executor.service';

export enum TicketListTabEnum {
  JOINED,
  CREATED,
}
@Component({
  selector: 'app-my-ticket-list',
  standalone: true,
  imports: [TabGroupComponent],
  templateUrl: './my-ticket-list.component.html',
  styleUrl: './my-ticket-list.component.scss',
})
export class MyTicketListComponent {
  public tabElements: NewButtonElement[] = [
    {
      selected: true,
      value: TicketListTabEnum.JOINED,
      label: '참여한 모임',
    },
    {
      selected: false,
      value: TicketListTabEnum.CREATED,
      label: '개최한 모임',
    },
  ];
  public joinedTickets: Ticket[] = [];
  public createdTickets: Ticket[] = [];
  constructor(
    public buttonElementsService: ButtonElementsService,
    private _apiExecutorService: ApiExecutorService
  ) {}
  public async ngOnInit() {
    //this.joinedTickets = await this._apiExecutorService.getJoinedMoims();
    //this.createdTickets = await this._apiExecutorService.getCreatedMoims();
  }
}
