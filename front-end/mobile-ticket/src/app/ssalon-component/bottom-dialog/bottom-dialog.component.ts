import { Component, EventEmitter, Input, Output } from '@angular/core';
import {
  NewButtonElement,
  SimpleToggleGroupComponent,
} from '../simple-toggle-group/simple-toggle-group.component';
import { MeetingCategoryComponent } from './meeting-category/meeting-category.component';
import { NgIf } from '@angular/common';
import { MeetingLocationComponent } from './meeting-location/meeting-location.component';
import { SquareButtonComponent } from '../square-button/square-button.component';

export enum BottomNavigatorType {
  MEETING_CATEGORY,
  MEETING_JOIN_CANCEL,
  MEETING_JOIN,
  MEETING_EDIT_COMPLETE,
  MEETING_KICKOUT,
  TICKET_MAKE_COMPLETE,
  TICKET_EDIT_COMPLETE,
  DIARY_MAKE_COMPLETE,
  MEETING_LOCATION,
}
@Component({
  selector: 'app-bottom-dialog',
  standalone: true,
  imports: [
    NgIf,
    MeetingCategoryComponent,
    MeetingLocationComponent,
    SquareButtonComponent,
  ],
  templateUrl: './bottom-dialog.component.html',
  styleUrl: './bottom-dialog.component.scss',
})
export class BottomDialogComponent {
  @Input() title: string = '';
  @Input() buttonElements: NewButtonElement[] = [];
  @Input() content: BottomNavigatorType = BottomNavigatorType.MEETING_CATEGORY;

  @Output() public readonly onClickEvent = new EventEmitter();
  @Output() public readonly onClickCompleteButtonEvent = new EventEmitter();
  public bottomNavigatorType = BottomNavigatorType;
  constructor() {}
  public onClickButton(value: number) {
    this.onClickEvent.emit({ contentType: this.content, value: value });
  }
  public onClickCompleteButton() {
    this.onClickCompleteButtonEvent.emit();
  }
}
