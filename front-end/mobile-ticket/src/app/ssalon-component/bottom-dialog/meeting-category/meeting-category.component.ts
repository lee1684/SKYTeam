import { Component, EventEmitter, Output } from '@angular/core';
import { SimpleToggleGroupComponent } from '../../simple-toggle-group/simple-toggle-group.component';
import { ButtonElementsService } from '../../../service/button-elements.service';

@Component({
  selector: 'app-meeting-category',
  standalone: true,
  imports: [SimpleToggleGroupComponent],
  templateUrl: './meeting-category.component.html',
  styleUrl: './meeting-category.component.scss',
})
export class MeetingCategoryComponent {
  @Output() public readonly onCategorySelectedEvent = new EventEmitter();
  public interestSelectionButtons = [
    {
      selected: false,
      value: 0,
      label: '운동',
      imgSrc: 'assets/interest-icons/excersize.png',
    },
    {
      selected: false,
      value: 1,
      label: '게임',
      imgSrc: 'assets/interest-icons/game.png',
    },
    {
      selected: false,
      value: 2,
      label: '음악',
      imgSrc: 'assets/interest-icons/music.png',
    },
    {
      selected: false,
      value: 3,
      label: '요리',
      imgSrc: 'assets/interest-icons/food.png',
    },
    {
      selected: false,
      value: 4,
      label: '독서',
      imgSrc: 'assets/interest-icons/book.png',
    },
  ];
  constructor(public buttonElementsService: ButtonElementsService) {}
  public onClilckButton(value: number) {
    this.onCategorySelectedEvent.emit(value);
  }
}
