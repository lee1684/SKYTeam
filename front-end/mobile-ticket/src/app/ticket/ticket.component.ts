import { Component, Input } from '@angular/core';
import { MobileTicketViewerComponent } from './mobile-ticket-viewer/mobile-ticket-viewer.component';
import { MobileTicketEditorComponent } from './mobile-ticket-editor/mobile-ticket-editor.component';

@Component({
  selector: 'app-ticket',
  standalone: true,
  imports: [MobileTicketViewerComponent, MobileTicketEditorComponent],
  templateUrl: './ticket.component.html',
  styleUrl: './ticket.component.scss',
})
export class TicketComponent {
  @Input() viewerHeight: number = 60;
  @Input() editorHeight: number = 40;
}
