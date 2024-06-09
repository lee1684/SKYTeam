import { Component, EventEmitter, Output } from '@angular/core';
import { NewButtonElement } from '../simple-toggle-group/simple-toggle-group.component';
import { NgClass } from '@angular/common';
import { MainTabEnum } from '../../main/main.component';

@Component({
  selector: 'app-bottom-navigator',
  standalone: true,
  imports: [NgClass],
  templateUrl: './bottom-navigator.component.html',
  styleUrl: './bottom-navigator.component.scss',
})
export class BottomNavigatorComponent {
  @Output() public readonly onClickNavigatorButtonEvent = new EventEmitter();

  public navigatorButtons: NewButtonElement[] = [
    { selected: true, value: MainTabEnum.MAIN, label: '메인' },
    { selected: false, value: MainTabEnum.SEARCH, label: '검색' },
    { selected: false, value: MainTabEnum.CREATE, label: '개최' },
    { selected: false, value: MainTabEnum.MINE, label: '증표' },
    { selected: false, value: MainTabEnum.PROFILE, label: '프로필' },
  ];
  consturctor() {}
  public ngAfterViewInit() {}
  public onClickNavigatorButton(value: MainTabEnum): void {
    this.onClickNavigatorButtonEvent.emit(value);
    this.navigatorButtons.forEach((element) => {
      element.selected = element.value === value;
    });
  }
  public getColor(value: MainTabEnum) {
    return this.navigatorButtons.find((element) => {
      return element.value === value;
    })!.selected
      ? '#000000'
      : '#BCBCBC';
  }
}
