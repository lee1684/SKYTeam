import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SceneSettingComponent } from './scene-setting/scene-setting.component';
import { Scene } from 'three';
import { MobileTicketEditorComponent } from './mobile-ticket-editor/mobile-ticket-editor.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SceneSettingComponent, MobileTicketEditorComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {}
