import { MTLLoader } from 'three/examples/jsm/loaders/MTLLoader.js';
import { OBJLoader } from 'three/examples/jsm/loaders/OBJLoader.js';
import { Face } from './face';
import {
  AxesHelper,
  Group,
  Mesh,
  MeshPhongMaterial,
  Object3D,
  Vector2,
  Vector3,
} from 'three';
import { ScenegraphService } from '../service/scenegraph.service';
import { ApiExecutorService } from '../service/api-executor.service';
import {
  DecorationInfo,
  SsalonConfigService,
} from '../service/ssalon-config.service';
import { ActivatedRoute } from '@angular/router';
import axios from 'axios';

export class MobileTicket {
  public mobileTicket: Object3D | null = null;
  public frontSide: Face | null = null;
  public frontDecorationInfo: DecorationInfo | boolean | null = null;
  public backSide: Face | null = null;
  public backDecorationinfo: DecorationInfo | boolean | null = null;
  constructor(
    private _apiExecutorService: ApiExecutorService,
    private _sceneGraphService: ScenegraphService
  ) {}

  /** get front, back decoration info */
  public async initMobileTicket(moimId: string): Promise<any> {
    /** 앞의 정보가 있다면 받아오기(앞면 정보는 항상 있을듯) */
    this.frontDecorationInfo = await this._apiExecutorService.getTicket(moimId);
    if (this.frontDecorationInfo === false)
      this.frontDecorationInfo = (
        await axios.get('assets/decoration.json')
      ).data;
    this.backDecorationinfo = await this._apiExecutorService.getDiary(moimId);
    if (this.backDecorationinfo === false)
      this.backDecorationinfo = (
        await axios.get('assets/decoration.json')
      ).data;
    this.loadMaterial();
  }

  /** load material */
  private loadMaterial(): void {
    const mtlLoader = new MTLLoader();
    mtlLoader.load('assets/untitled.mtl', (materials) => {
      materials.preload();
      this.loadObject(materials);
    });
  }

  /** load object */
  private loadObject(materials: any) {
    const loader = new OBJLoader();
    loader.setMaterials(materials);
    loader.load('assets/untitled.obj', (obj) => {
      this.initCardObject(obj);
    });
  }

  /** load face */
  private initCardObject(obj: Group): void {
    this.mobileTicket = obj.children[0];
    this.mobileTicket.scale.multiplyScalar(5);
    //roughness: 0.8,
    //color: 0xffffff,
    //metalness: 0.2,
    let backgroundColorString = (this.frontDecorationInfo as DecorationInfo)
      .backgroundColor;
    ((this.mobileTicket as Mesh).material as MeshPhongMaterial).color.set(
      parseInt(backgroundColorString?.slice(1)!, 16)
    );
    this.mobileTicket.rotateOnAxis(new Vector3(0, 0, 1), Math.PI / 2);
    this.mobileTicket.rotateOnAxis(new Vector3(1, 0, 0), Math.PI / 10);
    this.mobileTicket.rotateOnAxis(new Vector3(0, 1, 0), Math.PI / 10);
    this._sceneGraphService.scene!.add(this.mobileTicket!);
    //let a = new AxesHelper(100);
    //this._sceneGraphService.scene!.add(a);
    this.initSide('front');
    this.initSide('back');
    this.checkFaceVisible();
  }

  private initSide(side: string): void {
    if (side === 'front') {
      /** get decoration info from the server */
      this.frontSide = new Face(
        new Vector2(350, 600),
        'front',
        (this.frontDecorationInfo as DecorationInfo).fabric
      );
      this.frontSide.rotateZ(-Math.PI / 2);
      this.frontSide.position.add(new Vector3(0, 0, 0.6));
      this.mobileTicket?.add(this.frontSide);
      this.frontSide.scale.multiplyScalar(0.2);
    } else if (side === 'back') {
      /** get decoration info from the server */

      this.backSide = new Face(
        new Vector2(350, 600),
        'back',
        (this.backDecorationinfo as DecorationInfo).fabric
      );
      this.backSide.rotateZ(-Math.PI / 2);
      this.backSide.rotateY(-Math.PI);
      this.backSide.position.add(new Vector3(0, 0, -0.6));
      this.mobileTicket?.add(this.backSide);
      this.backSide.scale.multiplyScalar(0.2);
    }
  }

  public checkFaceVisible(): void {
    const cameraDirection = this._sceneGraphService.camera?.getWorldDirection(
      new Vector3(0, 0, 0)
    );
    const frontSideDirection =
      this.frontSide === null
        ? new Vector3(0, 0, 1)
        : new Vector3(0, 0, 1).applyQuaternion(this.frontSide.quaternion);
    if (cameraDirection!.dot(frontSideDirection!) >= 0) {
      this.frontSide!.visible = false;
      this.backSide!.visible = true;
    } else {
      this.frontSide!.visible = true;
      this.backSide!.visible = false;
    }
  }
}
