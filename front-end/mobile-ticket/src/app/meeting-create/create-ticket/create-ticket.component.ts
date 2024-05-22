import { Component, EventEmitter, Output } from '@angular/core';
import { SimpleToggleGroupComponent } from '../../ssalon-component/simple-toggle-group/simple-toggle-group.component';
import { ButtonElementsService } from '../../service/button-elements.service';

@Component({
  selector: 'app-create-ticket',
  standalone: true,
  imports: [SimpleToggleGroupComponent],
  templateUrl: './create-ticket.component.html',
  styleUrl: './create-ticket.component.scss',
})
export class CreateTicketComponent {
  @Output() public onClickToggleButtonEvent = new EventEmitter();
  constructor(public buttonElementsService: ButtonElementsService) {}
  public onClickToggleButton() {
    let clickedButton = this.buttonElementsService.createTicketTypeButtons.find(
      (button) => {
        return button.selected === true;
      }
    );
    console.log(clickedButton);
    if (clickedButton !== undefined) {
      this.onClickToggleButtonEvent.emit(clickedButton.value);
    } else {
      this.onClickToggleButtonEvent.emit(false);
    }
  }
}
