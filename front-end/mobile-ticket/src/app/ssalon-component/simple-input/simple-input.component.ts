import { NgIf } from '@angular/common';
import {
  ChangeDetectorRef,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { marked } from 'marked';
import { EmojiConvertor } from 'emoji-js';

@Component({
  selector: 'app-simple-input',
  standalone: true,
  imports: [NgIf, FormsModule],
  templateUrl: './simple-input.component.html',
  styleUrl: './simple-input.component.scss',
})
export class SimpleInputComponent {
  @ViewChild('textDiv', { static: false }) textDiv: ElementRef | null = null;
  @Input() label: string = '';
  @Input() extraLabel: string = '';
  @Input() extraLabelColor: string = '';
  @Input() placeholder: string = '';
  @Input() maxWidth: number = 300;
  @Input() longTextHeight: number = 300;
  @Input() type:
    | 'text'
    | 'longText'
    | 'datetime-local'
    | 'number'
    | 'category'
    | 'chat-input'
    | 'search-input' = 'text';
  @Input() innerText: string | number = '';
  @Input() enableCheckbox: boolean = false;
  @Input() checkBoxLabel: string = '';

  @Output() public readonly onChangeEvent = new EventEmitter();
  @Output() public readonly onClickEvent = new EventEmitter();
  @Output() public readonly onClickCheckboxEvent = new EventEmitter();
  @Output() public readonly onClickChatSendButtonEvent = new EventEmitter();
  @Output() public readonly onClickImgSendButtonEvent = new EventEmitter();
  @Output() public readonly onClickSearchButtonEvent = new EventEmitter();

  public isChecked: boolean = false;
  public convertedHTML: any = '';
  public showGeneralHTML: boolean = true;
  public today: any;

  constructor(private cd: ChangeDetectorRef) {}
  public ngOnInit() {
    if (this.textDiv != null) {
      if (this.textDiv.nativeElement.type === 'datetime-local') {
        this.today = new Date().toISOString().split('T')[0];
        this.textDiv.nativeElement.setAttribute('min', this.today);
      }
    }
  }
  public onClickInput(): void {
    this.onClickEvent.emit(this.innerText);
  }
  public onChangeInput(event: any): void {
    this.innerText = event.target.value;
    this.cd.detectChanges();
    this.onChangeEvent.emit(this.innerText);
  }
  public onClickCheckbox(event: any): void {
    this.isChecked = event.target.checked;
    if (this.type === 'longText') {
      if (this.isChecked) {
        this.showGeneralHTML = false;
        this.convertedHTML = this.convertMarkdown(this.innerText as string)
          .replace(/<p[^>]*>/g, '')
          .replace(/<\/p>/g, '');
      } else {
        this.showGeneralHTML = true;
      }
    }
    this.onClickCheckboxEvent.emit(this.isChecked);
  }
  public onClickChatSendButton(): void {
    this.onClickChatSendButtonEvent.emit();
  }

  public onClickImgSendButton(): void {
    this.onClickImgSendButtonEvent.emit();
  }

  public onClickSearchButton(): void {
    this.onClickSearchButtonEvent.emit();
  }

  public onClickMarkdown(): void {
    this.showGeneralHTML = false;
    this.convertedHTML = this.convertMarkdown(this.innerText as string)
      .replace(/<p[^>]*>/g, '')
      .replace(/<\/p>/g, '');
  }

  public onClickGeneralHTML(): void {
    this.showGeneralHTML = true;
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
