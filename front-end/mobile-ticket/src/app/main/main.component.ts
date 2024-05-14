import { Component, ElementRef, ViewChild } from '@angular/core';
import {
  NewButtonElement,
  SimpleToggleGroupComponent,
} from '../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { NgClass, NgFor } from '@angular/common';
import { ImageRowContainerComponent } from '../ssalon-component/image-row-container/image-row-container.component';
import { Router } from '@angular/router';
import { ThumbnailMakerService } from '../service/thumbnail-maker.service';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [
    NgClass,
    NgFor,
    SimpleToggleGroupComponent,
    ImageRowContainerComponent,
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss',
})
export class MainComponent {
  @ViewChild('ticketContainer')
  ticketContainer: ElementRef<HTMLDivElement> | null = null;
  public categorySelectionButtons: NewButtonElement[] = [
    {
      selected: true,
      value: 0,
      label: '전체',
      solid: true,
      fontColor: '#ffffff',
    },
    {
      selected: false,
      value: 1,
      label: '운동',
      solid: true,
      fontColor: '#ffffff',
    },
    {
      selected: false,
      value: 2,
      label: '게임',
      solid: true,
      fontColor: '#ffffff',
    },
    {
      selected: false,
      value: 3,
      label: '음악',
      solid: true,
      fontColor: '#ffffff',
    },
    {
      selected: false,
      value: 4,
      label: '음식',
      solid: true,
      fontColor: '#ffffff',
    },
    {
      selected: false,
      value: 5,
      label: '독서',
      solid: true,
      fontColor: '#ffffff',
    },
    {
      selected: false,
      value: 6,
      label: '영화',
      solid: true,
      fontColor: '#ffffff',
    },
  ];

  public navigatorSelectionButtons = [
    { selected: true, value: 0, label: '메인' },
    { selected: false, value: 1, label: '검색' },
    { selected: false, value: 2, label: '개최' },
    { selected: false, value: 3, label: '증표' },
    { selected: false, value: 4, label: '프로필' },
  ];
  constructor(
    private _router: Router,
    public thumbnailMakerService: ThumbnailMakerService
  ) {}
  public ngOnInit(): void {
    //document.getElementById('a')!.innerHTML =this.thumbnailMakerService.getThumbnail('#0090f2', 1, '1');
  }
  public onClickCategoryButton(value: number): void {
    this.ticketContainer!.nativeElement.scrollTop = value * 223;
  }
  public onClickNavigatorButton(value: number): void {}
  public getNavigatorButtonClass(selected: boolean) {
    return selected ? 'navigator-button-selected' : 'navigator-button';
  }
  public onClickTicket(value: number) {
    this._router.navigate(['/web/meeting-info'], {
      queryParams: { moimId: value },
    });
  }
}
