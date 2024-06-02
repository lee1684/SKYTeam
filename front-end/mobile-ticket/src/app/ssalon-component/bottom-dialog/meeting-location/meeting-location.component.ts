import { Component, EventEmitter, Output } from '@angular/core';
import { SimpleToggleGroupComponent } from '../../simple-toggle-group/simple-toggle-group.component';
import { ButtonElementsService } from '../../../service/button-elements.service';

@Component({
  selector: 'app-meeting-location',
  standalone: true,
  imports: [SimpleToggleGroupComponent],
  templateUrl: './meeting-location.component.html',
  styleUrl: './meeting-location.component.scss',
})
export class MeetingLocationComponent {
  @Output() public readonly onCategorySelectedEvent = new EventEmitter();
  constructor(public buttonElementsService: ButtonElementsService) {}
  public onClilckButton(value: number) {
    this.onCategorySelectedEvent.emit(value);
  }
}
