import { Component } from '@angular/core';
import { TopNavigatorComponent } from '../ssalon-component/top-navigator/top-navigator.component';
import { SimpleInputComponent } from '../ssalon-component/simple-input/simple-input.component';

@Component({
  selector: 'app-meeting-create',
  standalone: true,
  imports: [TopNavigatorComponent, SimpleInputComponent],
  templateUrl: './meeting-create.component.html',
  styleUrl: './meeting-create.component.scss',
})
export class MeetingCreateComponent {}
