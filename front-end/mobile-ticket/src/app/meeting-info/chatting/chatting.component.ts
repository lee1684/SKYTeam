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
  @Input() moimId: string = undefined as unknown as string;

  public myProfile: Profile = undefined as unknown as Profile;
  public participants: { nickname: string; profilePictureUrl: string }[] = [];
  public message: string = ''; // 메시지 초기화
  public isConnected: boolean = false;
  public isEntered: boolean = false;
  public messages: any[] = [];

  private stompClient: Client = undefined as unknown as Client;
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

    this.stompClient = new Client({
      brokerURL: 'wss://ssalon.co.kr/ws-stomp',
      connectHeaders: {
        Authorization: `Bearer ${this._apiExecutorService.token}`,
        moimId: this.moimId,
      },
    });
    this.stompClient.onConnect = (frame) => {
      console.log('Connected: ' + frame);
      this.isConnected = true;
      this.stompClient.subscribe(`/room/${this.moimId}`, (greeting: any) =>
        async function (this: ChattingComponent) {
          if (this.isEntered === false) {
            this.stompClient.publish({
              destination: `/send/${this.moimId}`,
              body: JSON.stringify({ message: this.simpleInput!.innerText }),
              headers: {
                Authorization: `Bearer ${this._apiExecutorService.token}`,
                MessageType: 'ENTER',
              },
            });
            this.isEntered = true;
          }
          let inComeMsg = JSON.parse(greeting.body);
          console.log(inComeMsg);
          if (inComeMsg.messageType === 'TALK') {
            this.messages.push(JSON.parse(greeting.body));
          } else if (inComeMsg.messageType === 'ENTER') {
            this.participants =
              await this._apiExecutorService.getChattingParticipants(
                this.moimId
              );
            console.log(this.participants);
          } else {
          }
        }.bind(this)
      );
    };

    this.stompClient.onStompError = (frame) => {
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
    this.stompClient.activate();
  }

  public disconnect() {
    if (this.stompClient) {
      this.stompClient.publish({
        destination: `/send/${this.moimId}`,
        body: JSON.stringify({ message: this.simpleInput!.innerText }),
        headers: {
          Authorization: `Bearer ${this._apiExecutorService.token}`,
          MessageType: 'LEAVE',
        },
      });
      this.stompClient.deactivate();
      this.isConnected = false;
      this.isEntered = false;
      this.messages = [];
    }
  }

  public sendMessage() {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.publish({
        destination: `/send/${this.moimId}`,
        body: JSON.stringify({ message: this.simpleInput!.innerText }),
        headers: {
          Authorization: `Bearer ${this._apiExecutorService.token}`,
          MessageType: 'TALK',
        },
      });
      this.simpleInput!.innerText = '';
    }
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

  @HostListener('keyup.enter', ['$event'])
  public onKeyUpEnter(event: KeyboardEvent) {
    if (this.simpleInput?.innerText !== '') {
      this.sendMessage();
    }
  }
}
