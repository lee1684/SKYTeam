import { Component } from '@angular/core';
import { SimpleToggleGroupComponent } from '../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { NgClass } from '@angular/common';
import { ImageRowContainerComponent } from '../ssalon-component/image-row-container/image-row-container.component';
import { Router } from '@angular/router';
import { ThumbnailMakerService } from '../service/thumbnail-maker.service';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [NgClass, SimpleToggleGroupComponent, ImageRowContainerComponent],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss',
})
export class MainComponent {
  public categorySelectionButtons = [
    { selected: true, value: 0, label: '전체' },
    { selected: false, value: 1, label: '운동' },
    { selected: false, value: 2, label: '게임' },
    { selected: false, value: 3, label: '음악' },
    { selected: false, value: 4, label: '음식' },
    { selected: false, value: 5, label: '독서' },
    { selected: false, value: 6, label: '영화' },
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
  public onClickNavigatorButton(value: number): void {}
  public getNavigatorButtonClass(selected: boolean) {
    return selected ? 'navigator-button-selected' : 'navigator-button';
  }
  public onClickTicket() {
    this._router.navigate(['/meeting-info'], { queryParams: { moimId: 1 } });
  }
}
