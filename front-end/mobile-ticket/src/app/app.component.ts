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
export class AppComponent {
  public moimId: number = -1;
  private readonly _token =
    'eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsInVzZXJuYW1lIjoia2FrYW8gMzQ1NzYwNDk5MCIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MTQyOTk5NzQsImV4cCI6MTcxNDMwMDU3NH0.NlK4TuuE70Jc7cA3eHXMOCfAUrot4TdOKnblQroYCJw';
  constructor() {
    this.setCookie('Cookie', `access=${this._token}`, 14);
  }
  private setCookie(name: string, value: string, exp: number) {
    var date = new Date();
    date.setTime(date.getTime() + exp * 24 * 60 * 60 * 1000);
    document.cookie =
      name + '=' + value + ';expires=' + date.toUTCString() + ';path=/';
  }
}
