import { Component } from '@angular/core';
import { SimpleInputComponent } from '../../ssalon-component/simple-input/simple-input.component';
import { Clock } from 'three';
import {
  ImageRowContainerComponent,
  Ticket,
} from '../../ssalon-component/image-row-container/image-row-container.component';
import { ApiExecutorService } from '../../service/api-executor.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-moim-search',
  standalone: true,
  imports: [NgIf, SimpleInputComponent, ImageRowContainerComponent],
  templateUrl: './moim-search.component.html',
  styleUrl: './moim-search.component.scss',
})
export class MoimSearchComponent {
  public searchTimeout: any;
  public runningTickets: Ticket[] = [];
  constructor(private _apiExecutorService: ApiExecutorService) {}
  public async ngOnInit() {
    let runningTickets = await this._apiExecutorService.getRunningMoims();
    this.runningTickets = runningTickets.content;
  }

  public isLoaded(): boolean {
    return this.runningTickets.length > 0;
  }

  public onInput(value: string) {
    clearTimeout(this.searchTimeout);
    this.searchTimeout = setTimeout(() => {
      this.onFindRelativeMoim(value);
    }, 2000);
  }

  public onFindRelativeMoim(value: string) {
    console.log('searching moim...');
  }
}
