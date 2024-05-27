import { Component, EventEmitter, Output } from '@angular/core';
import { ButtonElementsService } from '../../../../service/button-elements.service';
import { SimpleToggleGroupComponent } from '../../../../ssalon-component/simple-toggle-group/simple-toggle-group.component';

@Component({
  selector: 'app-create-diary',
  standalone: true,
  imports: [SimpleToggleGroupComponent],
  templateUrl: './create-diary.component.html',
  styleUrl: './create-diary.component.scss',
})
export class CreateDiaryComponent {
  @Output() public onClickToggleButtonEvent = new EventEmitter();
  constructor(public buttonElementsService: ButtonElementsService) {}
  public onClickToggleButton() {
    let clickedButton = this.buttonElementsService.createTicketTypeButtons.find(
      (button) => {
        return button.selected === true;
      }
    );
    if (clickedButton !== undefined) {
      this.onClickToggleButtonEvent.emit(clickedButton.value);
    } else {
      this.onClickToggleButtonEvent.emit(false);
    }
  }
}
