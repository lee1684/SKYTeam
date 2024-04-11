import { MTLLoader } from 'three/examples/jsm/loaders/MTLLoader';
import { OBJLoader } from 'three/examples/jsm/loaders/OBJLoader';
import { Face } from './face';
import {
  Group,
  Mesh,
  MeshPhongMaterial,
  Object3D,
  Vector2,
  Vector3,
} from 'three';
import { ScenegraphService } from './scenegraph.service';

export class MobileTicket {
  public mobileTicket: Object3D | null = null;
  public frontSide: Face | null = null;
  public backSide: Face | null = null;
  constructor(private _sceneGraphService: ScenegraphService) {}

  public initMobileTicket(): void {
    this.loadMaterial();
  }

  /** load material */
  private loadMaterial(): void {
    const mtlLoader = new MTLLoader();
    mtlLoader.load('assets/ticket.mtl', (materials) => {
      materials.preload();
      this.loadObject(materials);
    });
  }

  /** load object */
  private loadObject(materials: any) {
    const loader = new OBJLoader();
    loader.setMaterials(materials);
    // load a resource
    loader.load(
      'assets/ticket.obj',
      (obj) => {
        this.initCardObject(obj);
      },
      // called when loading is in progresses
      function (xhr) {
        console.log((xhr.loaded / xhr.total) * 100 + '% loaded');
      },
      // called when loading has errors
      function (error) {
        console.log('An error happened');
      }
    );
  }

  private initCardObject(obj: Group): void {
    this.mobileTicket = obj.children[0];
    //0xb76e79
    ((this.mobileTicket as Mesh).material as MeshPhongMaterial).color.set(
      0x0090f2
    );
    this.mobileTicket.rotateOnAxis(new Vector3(0, 0, 1), Math.PI / 2);
    this._sceneGraphService.scene!.add(this.mobileTicket!);
    this.initSide('front');
    this.initSide('back');
    this.checkFaceVisible();
  }

  private initSide(side: string): void {
    if (side === 'front') {
      this.frontSide = new Face(new Vector2(160, 90), 'front', 'decoration');
      this.frontSide.rotateZ(-Math.PI / 2);
      this.mobileTicket?.add(this.frontSide);
    } else if (side === 'back') {
      this.backSide = new Face(new Vector2(160, 90), 'back', 'decoration');
      this.backSide.rotateZ(-Math.PI / 2);
      this.backSide.rotateY(-Math.PI);
      this.mobileTicket?.add(this.backSide);
    }
  }

  public checkFaceVisible(): void {
    const cameraDirection = this._sceneGraphService.camera?.getWorldDirection(
      new Vector3(0, 0, 0)
    );
    const frontSideDirection = new Vector3(0, 0, 1);
    if (cameraDirection!.dot(frontSideDirection!) >= 0) {
      this.frontSide!.visible = false;
      this.backSide!.visible = true;
    } else {
      this.frontSide!.visible = true;
      this.backSide!.visible = false;
    }
  }
}
