import { Component, Input } from '@angular/core';
import { ApiExecutorService } from '../../service/api-executor.service';
import { NgFor, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { marked } from 'marked';
import { EmojiConvertor } from 'emoji-js';
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
  public convertedHTML: any = '';

  constructor(private _apiExecutorService: ApiExecutorService) {}
  public ngOnInit() {
    this.convertedHTML = this.convertMarkdown(this.message.message)
      .replace(/<p[^>]*>/g, '')
      .replace(/<\/p>/g, '');
    console.log(this.convertedHTML);
  }
  public getTime(date: string) {
    let dateObj = new Date(date);
    const kstOffset = 9 * 60; // 9시간을 분 단위로 변환
    const kstDate = new Date(dateObj.getTime() + kstOffset * 60 * 1000);

    // 시와 분을 포맷하여 출력
    const hours = kstDate.getHours().toString().padStart(2, '0');
    const minutes = kstDate.getMinutes().toString().padStart(2, '0');
    return `${hours} : ${minutes}`;
  }

  public convertMarkdown(message: string) {
    // emoji-js 인스턴스 생성
    const emoji = new EmojiConvertor();
    emoji.replace_mode = 'unified'; // 통일된 이모지 코드로 변환
    emoji.allow_native = true; // 네이티브 이모지를 허용

    const markedMessage = marked.parse(message) as string;
    const emojiConvertedHtml = emoji.replace_colons(markedMessage);

    return emojiConvertedHtml;
  }
}
