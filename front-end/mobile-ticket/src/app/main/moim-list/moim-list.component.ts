import { NgFor, NgIf } from '@angular/common';
import {
  Component,
  ElementRef,
  ViewChild,
  ViewChildren,
  QueryList,
} from '@angular/core';
import {
  NewButtonElement,
  SimpleToggleGroupComponent,
} from '../../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import {
  ImageRowContainerComponent,
  Ticket,
  TicketList,
} from '../../ssalon-component/image-row-container/image-row-container.component';
import { Router } from '@angular/router';
import { ButtonElementsService } from '../../service/button-elements.service';
import { ApiExecutorService } from '../../service/api-executor.service';

@Component({
  selector: 'app-moim-list',
  standalone: true,
  imports: [
    NgIf,
    NgFor,
    SimpleToggleGroupComponent,
    ImageRowContainerComponent,
  ],
  templateUrl: './moim-list.component.html',
  styleUrl: './moim-list.component.scss',
})
export class MoimListComponent {
  @ViewChild('ticketContainer')
  ticketContainer: ElementRef<HTMLDivElement> | null = null;
  @ViewChildren('rowContainers') rowContainers!: QueryList<ElementRef>;

  public isCategoryOrderUpdated: boolean = false;
  public ticketThumbnails: TicketList[] = [];
  constructor(
    private _apiExecutorService: ApiExecutorService,
    public buttonElementsService: ButtonElementsService
  ) {}
  public async ngOnInit() {
    /** 전체 */
    let recommendedTickets: Ticket[] =
      await this._apiExecutorService.getRecommendedMoims();
    this.isCategoryOrderUpdated =
      await this.buttonElementsService.updateCategoryOrder();
    let tickets = await this._apiExecutorService.getMoims();
    this.ticketThumbnails = tickets.content;
    this.ticketThumbnails.unshift({
      categoryName: '추천',
      meetingList: recommendedTickets,
    });
  }

  public ngOnDestroy() {
    this.isCategoryOrderUpdated = false;
  }
  public onClickCategoryButton(value: number): void {
    const rowContainer = this.rowContainers
      .toArray()
      .find(
        (rowContainer) => rowContainer.nativeElement.id === value.toString()
      );
    this.ticketContainer?.nativeElement.scrollTo({
      top: rowContainer?.nativeElement.offsetTop - 50,
      behavior: 'smooth',
    });
  }

  public isLoadedTickets(): boolean {
    if (this.ticketThumbnails.length > 0 && this.isCategoryOrderUpdated) {
      return true;
    } else return false;
  }
}
