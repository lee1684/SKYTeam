import { Injectable } from "@angular/core";


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
  public MOIM_ID: string = '12345678';
  public DIARY_ID: string = '12345678';
  constructor() {}
}
