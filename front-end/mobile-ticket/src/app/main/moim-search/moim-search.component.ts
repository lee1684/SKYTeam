import { Component } from '@angular/core';
<<<<<<< HEAD
import { SimpleInputComponent } from '../../ssalon-component/simple-input/simple-input.component';
import {
  ImageRowContainerComponent,
  Ticket,
} from '../../ssalon-component/image-row-container/image-row-container.component';
import { ApiExecutorService } from '../../service/api-executor.service';
import { NgIf } from '@angular/common';
=======
>>>>>>> develop

@Component({
  selector: 'app-moim-search',
  standalone: true,
<<<<<<< HEAD
  imports: [NgIf, SimpleInputComponent, ImageRowContainerComponent],
  templateUrl: './moim-search.component.html',
  styleUrl: './moim-search.component.scss',
})
export class MoimSearchComponent {
  public runningTickets: Ticket[] = [];
  public isSearching: boolean = false;
  constructor(private _apiExecutorService: ApiExecutorService) {}
  public async ngOnInit() {}

  public isLoaded(): boolean {
    return this.runningTickets.length > 0;
  }

  public async onInput(value: string) {
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
=======
  imports: [],
  templateUrl: './moim-search.component.html',
  styleUrl: './moim-search.component.scss'
})
export class MoimSearchComponent {

>>>>>>> develop
}
