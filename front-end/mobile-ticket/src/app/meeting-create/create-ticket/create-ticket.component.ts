import { Component } from '@angular/core';
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
  constructor(public buttonElementsService: ButtonElementsService) {}
}
