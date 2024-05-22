import { Component, HostListener } from '@angular/core';
import { TabGroupComponent } from '../../ssalon-component/tab-group/tab-group.component';
import { ButtonElementsService } from '../../service/button-elements.service';
import { NewButtonElement } from '../../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import {
  ImageRowContainerComponent,
  Ticket,
} from '../../ssalon-component/image-row-container/image-row-container.component';
import { ApiExecutorService } from '../../service/api-executor.service';
import { NgIf } from '@angular/common';

export enum TicketListTabEnum {
  RUNNING,
  ENDED,
}
@Component({
  selector: 'app-my-ticket-list',
  standalone: true,
  imports: [NgIf, TabGroupComponent, ImageRowContainerComponent],
  templateUrl: './my-ticket-list.component.html',
  styleUrl: './my-ticket-list.component.scss',
})
export class MyTicketListComponent {
  public tabElements: NewButtonElement[] = [
    {
      selected: true,
      value: TicketListTabEnum.RUNNING,
      label: '진행중인 모임',
    },
    {
      selected: false,
      value: TicketListTabEnum.ENDED,
      label: '끝난 모임',
    },
  ];
  public runningTickets: Ticket[] = [];
  public endedTickets: Ticket[] = [];

  public nowTab: TicketListTabEnum = TicketListTabEnum.RUNNING;
  public ticketListTabEnum = TicketListTabEnum;
  constructor(
    public buttonElementsService: ButtonElementsService,
    private _apiExecutorService: ApiExecutorService
  ) {}
  public async ngOnInit() {
    let runningTickets = await this._apiExecutorService.getRunningMoims();
    this.runningTickets = runningTickets.content;
    let endedTickets = await this._apiExecutorService.getEndedMoims();
    this.endedTickets = endedTickets.content;
  }

  public isLoaded(): boolean {
    let result: boolean = false;
    if (this.nowTab === TicketListTabEnum.RUNNING) {
      if (this.runningTickets !== undefined) {
        result = this.runningTickets.length > 0;
      } else {
        result = false;
      }
    } else if (this.nowTab === TicketListTabEnum.ENDED) {
      if (this.endedTickets !== undefined) {
        result = this.endedTickets.length > 0;
      } else {
        result = false;
      }
    } else {
      result = false;
    }
    return result;
  }

  public async onCickTabButton(value: TicketListTabEnum) {
    this.nowTab = value;
    if (this.nowTab === TicketListTabEnum.RUNNING) {
      this.runningTickets = (
        await this._apiExecutorService.getRunningMoims()
      ).content;
      console.log(await this._apiExecutorService.getRunningMoims());
    } else {
      this.endedTickets = (
        await this._apiExecutorService.getEndedMoims()
      ).content;
    }
  }

  public async getMoimInfos(hasNext: boolean) {}
}
