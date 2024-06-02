import { Component, ElementRef, Input, ViewChild } from '@angular/core';
import { ImageRowContainerComponent } from '../image-row-container/image-row-container.component';
import { NewButtonElement } from '../simple-toggle-group/simple-toggle-group.component';
import { NgIf } from '@angular/common';
import EmojiConvertor from 'emoji-js';
import { marked } from 'marked';

@Component({
  selector: 'app-simple-content',
  standalone: true,
  imports: [NgIf, ImageRowContainerComponent],
  templateUrl: './simple-content.component.html',
  styleUrl: './simple-content.component.scss',
})
export class SimpleContentComponent {
  @ViewChild('contentContainer', { static: true })
  contentContainer: ElementRef = undefined as unknown as ElementRef;

  @Input() title: string = '';
  @Input() content: string = '';
  @Input() images: NewButtonElement[] = [];
  @Input() imageHeight: number = 200;
  constructor() {}
  public ngOnInit(): void {
    this.contentContainer.nativeElement.innerHTML = this.convertMarkdown(
      this.content
    );
  }

  public convertMarkdown(message: string) {
    // emoji-js 인스턴스 생성
    const emoji = new EmojiConvertor();
    emoji.replace_mode = 'unified'; // 통일된 이모지 코드로 변환
    emoji.allow_native = true; // 네이티브 이모지를 허용

    const markedMessage = marked.parse(message) as string;
    const emojiConvertedHtml = emoji.replace_colons(markedMessage);

    return emojiConvertedHtml.replace(/<p[^>]*>/g, '').replace(/<\/p>/g, '');
  }
}
