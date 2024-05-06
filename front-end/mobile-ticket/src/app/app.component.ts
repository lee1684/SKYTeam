import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
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
    'eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsInVzZXJuYW1lIjoibmF2ZXIgbHphV19oUmprc1kzZXo1NUtJckpXdE9mMk1qTi1GZzJJbUF5SXBPOFNlcyIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MTQzODMxOTQsImV4cCI6MTcxNDM4Mzc5NH0.h6YWas--xg78ySTZ4Vwku6Kvkye2Z3lnAvm8iXJGXGs';
  constructor() {
    //this.setCookie('Cookie', `access=${this._token}`, 14);
  }
  private setCookie(name: string, value: string, exp: number) {
    var date = new Date();
    date.setTime(date.getTime() + exp * 24 * 60 * 60 * 1000);
    document.cookie =
      name + '=' + value + ';expires=' + date.toUTCString() + ';path=/';
  }
}
