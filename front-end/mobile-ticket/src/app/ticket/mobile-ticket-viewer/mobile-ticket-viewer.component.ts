import { Component, ViewChild, ElementRef, Input } from '@angular/core';
import { ScenegraphService } from '../../service/scenegraph.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-mobile-ticket-viewer',
  standalone: true,
  imports: [NgIf],
  templateUrl: './mobile-ticket-viewer.component.html',
  styleUrl: './mobile-ticket-viewer.component.scss',
})
export class MobileTicketViewerComponent {
  @ViewChild('rendererContainer', { static: false })
  rendererContainer: ElementRef | null = null;

  @Input() moimId: string = '';
  public majorFace: string = 'front';
  public isEditing: boolean = false;
  constructor(private _sceneGraphService: ScenegraphService) {}
  public ngOnInit(): void {}
  public ngAfterViewInit(): void {
    this._sceneGraphService.nativeElement =
      this.rendererContainer!.nativeElement;
    this._sceneGraphService.initThree(this.moimId);
  }

  public ngOnDestroy(): void {
    this._sceneGraphService.destroy();
  }
}
