import { NgIf, Location } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-top-navigator',
  standalone: true,
  imports: [NgIf],
  templateUrl: './top-navigator.component.html',
  styleUrl: './top-navigator.component.scss',
})
export class TopNavigatorComponent {
  @Input() title: string = '';
  @Input() shareButton: boolean = false;

  @Output() backButtonClick = new EventEmitter();
  @Output() shareButtonClick = new EventEmitter();
  public comeFrom: string = '';

  constructor(private _location: Location) {}
  public onClickBackButton(): void {
    this._location.back();
  }
  public onClickShareButton(): void {
    this.shareButtonClick.emit();
  }
}
