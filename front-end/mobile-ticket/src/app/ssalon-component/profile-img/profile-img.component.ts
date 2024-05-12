import { NgIf } from '@angular/common';
import { Component, input, Input } from '@angular/core';

@Component({
  selector: 'app-profile-img',
  standalone: true,
  imports: [NgIf],
  templateUrl: './profile-img.component.html',
  styleUrl: './profile-img.component.scss',
})
export class ProfileImgComponent {
  @Input() imgSize = 96;
  @Input() imgSrc = 'assets/heart.png';
  @Input() isEditable = true;
  constructor() {}
}
