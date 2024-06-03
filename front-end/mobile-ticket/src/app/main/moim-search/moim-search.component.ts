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
  public isSearching: boolean = false;
  constructor(private _apiExecutorService: ApiExecutorService) {}
  public async ngOnInit() {}

  public isLoaded(): boolean {
    return this.runningTickets.length > 0;
  }

  public async onInput(value: string) {
    clearTimeout(this.searchTimeout);
    this.runningTickets = [];
    this.isSearching = true;
    await this.onFindRelativeMoim(value);
    this.isSearching = false;
  }

  public async onFindRelativeMoim(value: string) {
    this.runningTickets = (
      await this._apiExecutorService.searchMoims(value)
    ).content;
  }
}
