import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SceneSettingComponent } from './scene-setting/scene-setting.component';
import { Scene } from 'three';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SceneSettingComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

}
