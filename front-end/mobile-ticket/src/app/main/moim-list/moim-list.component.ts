import { NgFor, NgIf } from '@angular/common';
<<<<<<< HEAD
<<<<<<< HEAD
import { Component, ElementRef, ViewChild, ViewChildren, QueryList } from '@angular/core';
=======
import {
  Component,
  ElementRef,
  ViewChild,
  ViewChildren,
  QueryList,
} from '@angular/core';
>>>>>>> angular
=======
import { Component, ElementRef, ViewChild, ViewChildren, QueryList } from '@angular/core';
>>>>>>> develop
import {
  NewButtonElement,
  SimpleToggleGroupComponent,
} from '../../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import {
  ImageRowContainerComponent,
  Ticket,
<<<<<<< HEAD
<<<<<<< HEAD
=======
  TicketList,
>>>>>>> angular
=======
>>>>>>> develop
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

<<<<<<< HEAD
<<<<<<< HEAD
  public ticketThumbnails: Ticket[][] = [];
=======
  public isCategoryOrderUpdated: boolean = false;
  public ticketThumbnails: TicketList[] = [];
>>>>>>> angular
=======
  public ticketThumbnails: Ticket[][] = [];
>>>>>>> develop
  constructor(
    private _apiExecutorService: ApiExecutorService,
    public buttonElementsService: ButtonElementsService
  ) {}
  public async ngOnInit() {
    /** 전체 */
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> develop
    let tickets = await this._apiExecutorService.getMoims();
    this.ticketThumbnails.push(tickets.content);

    for (
      let i = 0;
      i < this.buttonElementsService.interestSelectionButtons.length;
      i++
    ) {
      let tickets = await this._apiExecutorService.getMoims(
        this.buttonElementsService.interestSelectionButtons[i].label
      );
      this.ticketThumbnails.push(tickets.content);
    }
  }
  public onClickCategoryButton(value: number): void {
    const rowContainer = this.rowContainers.toArray().find(rowContainer => rowContainer.nativeElement.id === value.toString());
    this.ticketContainer!.nativeElement.scrollTo({
      top: rowContainer!.nativeElement.offsetTop - 50,
<<<<<<< HEAD
=======
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
>>>>>>> angular
=======
>>>>>>> develop
      behavior: 'smooth',
    });
  }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> develop
  public isLoadedTickets(i: number): boolean {
    if (this.ticketThumbnails.length > 0) {
      if (this.ticketThumbnails[i] !== undefined) {
        if (this.ticketThumbnails[i].length > 0) {
          return true;
        } else return false;
      } else return false;
<<<<<<< HEAD
=======
  public isLoadedTickets(): boolean {
    if (this.ticketThumbnails.length > 0 && this.isCategoryOrderUpdated) {
      return true;
>>>>>>> angular
=======
>>>>>>> develop
    } else return false;
  }
}
