import { NgFor } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import {
  NewButtonElement,
  SimpleToggleGroupComponent,
} from '../../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import {
  ImageRowContainerComponent,
  Ticket,
} from '../../ssalon-component/image-row-container/image-row-container.component';
import { Router } from '@angular/router';
import { ButtonElementsService } from '../../service/button-elements.service';
import { ApiExecutorService } from '../../service/api-executor.service';

@Component({
  selector: 'app-moim-list',
  standalone: true,
  imports: [NgFor, SimpleToggleGroupComponent, ImageRowContainerComponent],
  templateUrl: './moim-list.component.html',
  styleUrl: './moim-list.component.scss',
})
export class MoimListComponent {
  @ViewChild('ticketContainer')
  ticketContainer: ElementRef<HTMLDivElement> | null = null;

  public ticketThumbnails: Ticket[] = [];
  constructor(
    private _apiExecutorService: ApiExecutorService,
    private _router: Router,
    public buttonElementsService: ButtonElementsService
  ) {}
  public async ngOnInit() {
    let tickets = await this._apiExecutorService.getMoims();
    this.ticketThumbnails = tickets.content;
    console.log(this.ticketThumbnails);
  }
  public onClickCategoryButton(value: number): void {
    this.ticketContainer!.nativeElement.scrollTo({
      top: 200,
    });
  }
  public onClickTicket(value: number) {
    this._router.navigate(['/web/meeting-info'], {
      queryParams: { moimId: value },
    });
  }
}
