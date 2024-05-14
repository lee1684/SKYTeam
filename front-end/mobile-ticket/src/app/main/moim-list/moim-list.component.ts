import { NgFor } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import {
  NewButtonElement,
  SimpleToggleGroupComponent,
} from '../../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { ImageRowContainerComponent } from '../../ssalon-component/image-row-container/image-row-container.component';
import { Router } from '@angular/router';

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
  public categorySelectionButtons: NewButtonElement[] = [
    {
      selected: true,
      value: 0,
      label: '전체',
      solid: true,
      unselectedBackgroundColor: '#F8F8F8',
      unselectedFontColor: '#000000',
      selectedBackgroundColor: '#000000',
      selectedFontColor: '#ffffff',
    },
    {
      selected: false,
      value: 1,
      label: '운동',
      solid: true,
      unselectedBackgroundColor: '#F8F8F8',
      unselectedFontColor: '#000000',
      selectedBackgroundColor: '#000000',
      selectedFontColor: '#ffffff',
    },
    {
      selected: false,
      value: 2,
      label: '게임',
      solid: true,
      unselectedBackgroundColor: '#F8F8F8',
      unselectedFontColor: '#000000',
      selectedBackgroundColor: '#000000',
      selectedFontColor: '#ffffff',
    },
    {
      selected: false,
      value: 3,
      label: '음악',
      solid: true,
      unselectedBackgroundColor: '#F8F8F8',
      unselectedFontColor: '#000000',
      selectedBackgroundColor: '#000000',
      selectedFontColor: '#ffffff',
    },
    {
      selected: false,
      value: 4,
      label: '음식',
      solid: true,
      unselectedBackgroundColor: '#F8F8F8',
      unselectedFontColor: '#000000',
      selectedBackgroundColor: '#000000',
      selectedFontColor: '#ffffff',
    },
    {
      selected: false,
      value: 5,
      label: '독서',
      solid: true,
      unselectedBackgroundColor: '#F8F8F8',
      unselectedFontColor: '#000000',
      selectedBackgroundColor: '#000000',
      selectedFontColor: '#ffffff',
    },
    {
      selected: false,
      value: 6,
      label: '영화',
      solid: true,
      unselectedBackgroundColor: '#F8F8F8',
      unselectedFontColor: '#000000',
      selectedBackgroundColor: '#000000',
      selectedFontColor: '#ffffff',
    },
  ];
  constructor(private _router: Router) {}
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
