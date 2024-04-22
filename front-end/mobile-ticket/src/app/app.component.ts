import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MobileTicketEditorComponent } from './ticket/mobile-ticket-editor/mobile-ticket-editor.component';
import { MobileTicketViewerComponent } from './ticket/mobile-ticket-viewer/mobile-ticket-viewer.component';
import { TicketComponent } from './ticket/ticket.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, TicketComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {}
