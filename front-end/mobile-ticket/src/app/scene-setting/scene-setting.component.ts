import { Component, ElementRef, HostListener, ViewChild } from '@angular/core';
import { ScenegraphService } from '../service/scenegraph.service';
@Component({
  selector: 'app-scene-setting',
  standalone: true,
  imports: [],
  templateUrl: './scene-setting.component.html',
  styleUrl: './scene-setting.component.scss'
})
export class SceneSettingComponent {
  @ViewChild('rendererContainer', { static: true }) rendererContainer: ElementRef | null = null;
  constructor(
    private _sceneGraphService: ScenegraphService,
  ) { 
  }
  ngAfterViewInit(): void {
    this._sceneGraphService.nativeElement = this.rendererContainer!.nativeElement;
    this._sceneGraphService.initThree();
  }

  ngOnDestroy(): void {
    this._sceneGraphService.destroy();
  }

  public onClickFocusFrontButton(): void{
    this._sceneGraphService.focusFront();
  }
  @HostListener('window:mousedown', ['$event'])
  public onMouseDown(event: MouseEvent): void{
    console.log("a")
  }
  

}
