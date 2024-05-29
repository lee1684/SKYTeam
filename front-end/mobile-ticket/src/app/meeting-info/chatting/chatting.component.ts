import { NgIf, NgFor } from '@angular/common';
import {
  Component,
  ElementRef,
  HostListener,
  Input,
  ViewChild,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  ApiExecutorService,
  Profile,
} from '../../service/api-executor.service';
import { Client, Message } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { SimpleInputComponent } from '../../ssalon-component/simple-input/simple-input.component';
import { ChatContainerComponent } from '../../ssalon-component/chat-container/chat-container.component';

@Component({
  selector: 'app-chatting',
  standalone: true,
  imports: [
    NgIf,
    NgFor,
    FormsModule,
    SimpleInputComponent,
    ChatContainerComponent,
  ],
  templateUrl: './chatting.component.html',
  styleUrl: './chatting.component.scss',
})
export class ChattingComponent {
  @ViewChild('simpleInput', { static: false })
  simpleInput: SimpleInputComponent | null = null;
  @ViewChild('msgContainer', { static: false })
  msgContainer: ElementRef | null = null;
  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;
  @Input() moimId: string = undefined as unknown as string;

  public myProfile: Profile = undefined as unknown as Profile;
  public participants: { nickname: string; profilePictureUrl: string }[] = [];
  public message: string = ''; // 메시지 초기화
  public isConnected: boolean = false;
  public isEntered: boolean = false;
  public messages: any[] = [];

  private _imageUrl: string = '';
  private _file: File = undefined as unknown as File;
  private _stompClient: Client = undefined as unknown as Client;
  constructor(private _apiExecutorService: ApiExecutorService) {
    // 새로고침 시 disconnect 설정
    window.addEventListener('beforeunload', () => {
      console.log('hello');
      this.disconnect();
    });
  }

  public async ngOnInit() {
    this.myProfile = this._apiExecutorService.myProfile;
    let tempMessages = await this._apiExecutorService.getLastMessages(
      this.moimId
    );
    this.messages = tempMessages.filter((message: any) => {
      return message.messageType === 'TALK';
    });

    this._stompClient = new Client({
      brokerURL: 'wss://ssalon.co.kr/ws-stomp',
      connectHeaders: {
        Authorization: `Bearer ${this._apiExecutorService.token}`,
        moimId: this.moimId,
      },
    });

    this._stompClient.onConnect = (frame) => {
      console.log('Connected: ' + frame);
      this.isConnected = true;

      // 채팅방에 입장 메시지를 보냅니다.
      if (!this.isEntered) {
        this._stompClient.publish({
          destination: `/send/${this.moimId}`,
          body: JSON.stringify({ message: this.simpleInput!.innerText }),
          headers: {
            Authorization: `Bearer ${this._apiExecutorService.token}`,
            MessageType: 'ENTER',
          },
        });
        this.isEntered = true;
        console.log('입장');
      }

      this._stompClient.subscribe(`/room/${this.moimId}`, (greeting: any) => {
        let inComeMsg = JSON.parse(greeting.body);
        console.log(inComeMsg);
        if (inComeMsg.messageType === 'TALK') {
          this.messages.push(JSON.parse(greeting.body));
        } else if (inComeMsg.messageType === 'ENTER') {
          this.updateParticipants();
        } else {
          this.updateParticipants();
        }
      });
    };

    this._stompClient.onStompError = (frame) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };
    this.connect();
  }
  public ngAfterViewChecked() {
    this.msgContainer!.nativeElement.scrollTop =
      this.msgContainer!.nativeElement.scrollHeight;
  }
  public ngOnDestroy() {
    this.disconnect();
  }

  public connect() {
    this._stompClient.activate();
  }

  public disconnect() {
    if (this._stompClient) {
      this._stompClient.publish({
        destination: `/send/disconnect`,
        body: JSON.stringify({ message: this.simpleInput!.innerText }),
        headers: {
          Authorization: `Bearer ${this._apiExecutorService.token}`,
          MessageType: 'LEAVE',
        },
      });
      this._stompClient.deactivate();
      this.isConnected = false;
      this.isEntered = false;
      this.messages = [];
    }
  }

  public sendMessage() {
    if (this._stompClient && this._stompClient.connected) {
      if (this._file) {
        // 이미지 s3에 업로드
        this.uploadFile(this._file)
          .then(() => {
            // 이미지 업로드 완료 후 메시지 전송
            this.publishMessage();
            this.flushAfterSendMessage();
          })
          .catch((error) => {
            // 이미지 업로드 실패 시 에러 처리
            console.error('File upload error:', error);
          });
      } else {
        // 이미지가 null인 경우
        this.publishMessage();
        this.flushAfterSendMessage();
      }
    }
  }

  private publishMessage() {
    this._stompClient.publish({
      destination: `/send/${this.moimId}`,
      body: JSON.stringify({
        message: this.simpleInput!.innerText,
        imageUrl: this._imageUrl,
      }),
      headers: {
        Authorization: `Bearer ${this._apiExecutorService.token}`,
        MessageType: 'TALK',
      },
    });
  }

  private flushAfterSendMessage() {
    this.simpleInput!.innerText = '';
    this._imageUrl = '';
    this.fileInput.nativeElement.value = '';
  }

  public isMyMsg(message: any) {
    if (
      this.myProfile.nickname === message.nickname &&
      this.myProfile.email === message.email
    ) {
      return 'right';
    } else {
      return 'left';
    }
  }

  public isSameSender(msg: any, i: number) {
    if (i === 0) return false;
    return this.messages[i - 1].nickname === msg.nickname;
  }

  public async updateParticipants() {
    this.participants = await this._apiExecutorService.getChattingParticipants(
      this.moimId
    );
  }

  @HostListener('keyup.enter', ['$event'])
  public onKeyUpEnter(event: KeyboardEvent) {
    if (this.simpleInput?.innerText !== '') {
      this.sendMessage();
    }
  }

  public sendImage() {
    const fileInput = document.createElement('input');
    fileInput.type = 'file';
    fileInput.accept = 'image/*';
    fileInput.onchange = async function (this: ChattingComponent) {
      this._imageUrl = '';
      if (fileInput.files && fileInput.files.length > 0) {
        this._file = fileInput.files[0];
        this._imageUrl = await this.uploadFile(this._file);
        this.sendMessage();
      }
    }.bind(this);
    fileInput.click();
  }

  private async uploadFile(file: File) {
    const body = new FormData();

    // 파일들을 FormData 객체에 추가
    body.append('files', file);

    // 서버로 파일 업로드 요청 보내기
    const result = await this._apiExecutorService.uploadGeneralImage(body);
    let key = Object.keys(result.mapURI)[0];
    return result.mapURI[key];
  }
}
