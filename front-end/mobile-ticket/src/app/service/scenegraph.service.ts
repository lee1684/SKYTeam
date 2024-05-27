import * as THREE from 'three';
import { CSS3DRenderer } from 'three/examples/jsm/renderers/CSS3DRenderer.js';
import { ArcballControls } from 'three/examples/jsm/controls/ArcballControls.js';
import { MobileTicket } from '../mobile-ticket/mobile-ticket';
import { SsalonConfigService } from './ssalon-config.service';
import { ApiExecutorService } from './api-executor.service';
import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
@Injectable({
  providedIn: 'root',
})
export class ScenegraphService {
  public scene: THREE.Scene | null = null;
  public camera: THREE.PerspectiveCamera | null = null;
  public lights: { [key: string]: any } = {};
  public nativeElement: any | null = null;
  public webGLRenderer: THREE.WebGLRenderer | null = null;
  public css3dRenderer: CSS3DRenderer | null = null;
  private arcballControls: ArcballControls | null = null;

  public mobileTicket: MobileTicket | null = null;
  public mobileTicketAutoRotate: boolean = false;
  constructor(
    private _route: ActivatedRoute,
    private _apiExecutorService: ApiExecutorService,
    private _ssalonConfigService: SsalonConfigService
  ) {}
  public initThree(moimId: string): void {
    const width = this.nativeElement!.clientWidth;
    const height = this.nativeElement!.clientHeight;

    /** create Scene */
    this.scene = new THREE.Scene();
    this.scene.background = new THREE.Color(0xffffff); //

    /** create Perspective Camera */
    this.camera = new THREE.PerspectiveCamera(75, width / height, 0.1, 2000);
    this.camera!.position.z = 600;

    /** create Lights
     * because ArcballControl transforms the camera,
     * if the camera goes behind of the object, we can't see the object.
     */
    this.createLight('pointLight1', new THREE.Vector3(0, 0, 300));
    this.createLight('pointLight2', new THREE.Vector3(0, 0, -300));
    this.createLight('pointLight3', new THREE.Vector3(0, 400, 300));

    /** create and init Renderers */
    this.initCss3dRenderer(width, height);
    this.initWebGLRenderer(width, height);

    /** create ArcballControl */
    this.createArcballControls();
    this.mobileTicket = new MobileTicket(this._apiExecutorService, this);
    this.mobileTicket!.initMobileTicket(moimId);
    startAnimation(this);
  }

  private createLight(lightName: string, position: THREE.Vector3) {
    this.lights[lightName] = new THREE.PointLight(0xffffff);
    this.lights[lightName].position.copy(position);
    this.lights[lightName].intensity = 10;
    this.lights[lightName].decay = 0.3;
    this.lights[lightName].lookAt(0, 0, 0);
    this.scene!.add(this.lights[lightName]);
  }

  private initWebGLRenderer(width: number, height: number): void {
    this.webGLRenderer = new THREE.WebGLRenderer({ antialias: true });
    this.webGLRenderer.setPixelRatio(window.devicePixelRatio);
    this.webGLRenderer.autoClear = false;
    this.webGLRenderer.domElement.style.touchAction = 'none';
    this.webGLRenderer.setSize(width, height);
    this.nativeElement.appendChild(this.webGLRenderer.domElement);
  }

  private initCss3dRenderer(width: number, height: number): void {
    this.css3dRenderer = new CSS3DRenderer();
    this.css3dRenderer.domElement.style.touchAction = 'none';
    this.css3dRenderer.domElement.style.position = 'absolute';
    this.css3dRenderer.setSize(width, height);
    this.nativeElement.appendChild(this.css3dRenderer.domElement);
  }

  public destroy() {
    this.nativeElement.removeChild(this.webGLRenderer!.domElement);
    this.nativeElement.removeChild(this.css3dRenderer!.domElement);
  }

  private createArcballControls(): void {
    this.arcballControls = new ArcballControls(
      this.camera!,
      this.css3dRenderer!.domElement,
      this.scene!
    );
    this.arcballControls.setGizmosVisible(false);
    this.arcballControls.enableAnimations = true;
    this.arcballControls.dampingFactor = 2;
    this.arcballControls.addEventListener('change', () => {
      this.css3dRenderer!.render(this.scene!, this.camera!);
      this.mobileTicket?.checkFaceVisible();
    });
    this.arcballControls.enabled = true;
  }

  public onRender(): void {
    this.webGLRenderer!.render(this.scene!, this.camera!);
    this.css3dRenderer!.render(this.scene!, this.camera!);
  }

  public focusFront(): void {
    this.arcballControls!.reset();
  }

  public rotateCard(): void {
    this.mobileTicket!.mobileTicket?.rotateX(0.001);
    this.mobileTicket!.mobileTicket?.rotateY(0.001);
    this.mobileTicket!.mobileTicket?.rotateZ(0.001);
    if (
      this.mobileTicket?.frontSide !== null &&
      this.mobileTicket?.backSide !== null
    ) {
      this.mobileTicket?.checkFaceVisible();
    }
  }
}

/** 애니메이션 함수 */
const startAnimation = function (sceneSetting: ScenegraphService) {
  const clock = new THREE.Clock();
  const animationFrame = function () {
    if (sceneSetting.mobileTicketAutoRotate) {
      sceneSetting.rotateCard();
    }
    sceneSetting.onRender();
    requestAnimationFrame(animationFrame);
  };

  animationFrame();
};
