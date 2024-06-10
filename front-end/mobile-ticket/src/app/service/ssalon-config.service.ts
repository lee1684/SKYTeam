import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

export interface DecorationInfo {
  thumbnailUrl: string;
  backgroundColor: string;
  fabric: any;
}

@Injectable({
  providedIn: 'root',
})
export class SsalonConfigService {
  /** scene 관련 */
  public SCENE_BACKGROUND_COLOR: string = '#FFFFFF';
  /** 모임 및 다이어리 관련 */
  public MOIM_ID: string = '';
  public DIARY_ID: string = '';
  public FACE_TYPE: string = '';
  public VIEW_TYPE: string = '';

  public TOP_NAVIGATOR_HEIGHT: number = 60;
  public BOTTOM_BUTTON_HEIGHT: number = 80;
  constructor() {}
}
