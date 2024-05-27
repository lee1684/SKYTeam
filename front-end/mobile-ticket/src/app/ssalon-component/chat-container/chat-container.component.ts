import { Component, Input } from '@angular/core';
import { ApiExecutorService } from '../../service/api-executor.service';
import { NgFor, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-chat-container',
  standalone: true,
  imports: [NgIf, NgFor, FormsModule],
  templateUrl: './chat-container.component.html',
  styleUrl: './chat-container.component.scss',
})
export class ChatContainerComponent {
  @Input() contentOnly: boolean = false;
  @Input() content: string = undefined as unknown as string;
  @Input() message: any = undefined as unknown as string;
  @Input() align: string = 'left';
  constructor(private _apiExecutorService: ApiExecutorService) {}
  public ngOnInit() {}
  public getTime(date: string) {
    let dateObj = new Date(date);
    const kstOffset = 9 * 60; // 9시간을 분 단위로 변환
    const kstDate = new Date(dateObj.getTime() + kstOffset * 60 * 1000);

    // 시와 분을 포맷하여 출력
    const hours = kstDate.getHours().toString().padStart(2, '0');
    const minutes = kstDate.getMinutes().toString().padStart(2, '0');
    return `${hours} : ${minutes}`;
  }
}
