import { NgClass, NgFor } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NewButtonElement } from '../simple-toggle-group/simple-toggle-group.component';

@Component({
  selector: 'app-tab-group',
  standalone: true,
  imports: [NgFor, NgClass],
  templateUrl: './tab-group.component.html',
  styleUrl: './tab-group.component.scss',
})
export class TabGroupComponent {
  @Input() tabs: NewButtonElement[] = [
    { selected: true, value: 0, label: 'Tab 1' },
    { selected: false, value: 1, label: 'Tab 2' },
    { selected: false, value: 2, label: 'Tab 3' },
  ];

  @Output() public readonly onClickTabEvent = new EventEmitter();
  constructor() {}

  public onClickTab(value: number): void {
    this.onClickTabEvent.emit(value);
    this.tabs = this.tabs.map((tab) => {
      return {
        ...tab,
        selected: tab.value === value,
      };
    });
  }
  public getClass(value: number): string {
    return this.tabs.find((tab) => {
      return tab.value === value;
    })!.selected
      ? 'selected-tab'
      : 'unselected-tab';
  }
  public getButtonWidth(): number {
    return window.innerWidth / this.tabs.length;
  }
}
