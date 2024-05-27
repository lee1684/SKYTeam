import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  Output,
  QueryList,
  ViewChildren,
} from '@angular/core';
import { NgFor, NgIf } from '@angular/common';

export interface StatusElement {
  imgSrc: string;
  value: number;
  label: string;
  status: boolean;
}
@Component({
  selector: 'app-circle-toggle-status-group',
  standalone: true,
  imports: [NgFor, NgIf],
  templateUrl: './circle-toggle-status-group.component.html',
  styleUrl: './circle-toggle-status-group.component.scss',
})
export class CircleToggleStatusGroupComponent {
  @ViewChildren('circleStatus')
  buttons: QueryList<ElementRef> | null = null;

  @Input() public elements: StatusElement[] = [];
  @Input() public outerSize: number = 40;
  @Input() public columnNum: number = 10;
  @Input() public isLabelVisible: boolean = true;
  @Output() public readonly onClickToggleButtonEvent = new EventEmitter();

  public modifiedElements: StatusElement[][] = [];

  constructor() {}
  public ngOnInit(): void {
    let result = [];
    for (let i = 0; i < this.elements.length; i += this.columnNum) {
      result.push(this.elements.slice(i, i + this.columnNum));
    }
    this.modifiedElements = result;
  }
  public ngAfterViewInit(): void {}

  public changeStatus(value: number, status: boolean): void {
    this.elements.find((element) => element.value === value)!.status = status;
  }
  public getBorderColor(status: boolean): string {
    return status ? '#4DAF50' : '#F44336';
  }
}
