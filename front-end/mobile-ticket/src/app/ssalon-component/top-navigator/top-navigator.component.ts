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
  @Input() backButton: boolean = true;
  @Input() defaultBack: boolean = true;

  @Output() backButtonClickEvent = new EventEmitter();
  @Output() shareButtonClickEvent = new EventEmitter();
  public comeFrom: string = '';

  constructor(private _location: Location) {}
  public onClickBackButton(): void {
    if (this.defaultBack === true) this._location.back();
    else this.backButtonClickEvent.emit();
  }
  public onClickShareButton(): void {
    this.shareButtonClickEvent.emit();
  }
}
