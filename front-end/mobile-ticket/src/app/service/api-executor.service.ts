import axios, { AxiosInstance } from 'axios';
import { SsalonConfigService } from './ssalon-config.service';
import { Injectable } from '@angular/core';
import { RegisterUserInfo } from '../onboarding/onboarding.component';

export const setToken = function (
  token: string,
  apiExecutorService: ApiExecutorService
) {
  apiExecutorService.setToken(token);
};

export interface Profile {
  nickname: string;
  profilePictureUrl: string;
  gender: 'M' | 'F' | 'G';
  address: string;
  introduction: string;
  interests: string[];
  email: string;
}

@Injectable({
  providedIn: 'root',
})
export class ApiExecutorService {
  public apiExecutor: AxiosInstance | null = null;
  public apiExecutorJson: AxiosInstance | null = null;
  public apiURL: string = 'https://ssalon.co.kr/api';
  //public apiURL: string = 'http://localhost:8080/api';
  public tokens = {};
  public token: string = '';
  public refreshToken: string = '';
  public myProfile: Profile = undefined as unknown as Profile;
  constructor(private _ssalonConfigService: SsalonConfigService) {
    this.initApiExecutor();
  }

  public async ngOnInit() {}

  public setToken(token: string) {
    this.token = token;
  }

  private initApiExecutor() {
    this.apiExecutor = axios.create({
      baseURL: this.apiURL,
      headers: {
        'Content-Type': 'multipart/form-data',
        'Access-Control-Allow-Origin': '*',
        Authorization: `Bearer ${this.token}`,
        withCredentials: true,
      },
    });
    this.apiExecutorJson = axios.create({
      baseURL: this.apiURL,
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        Authorization: `Bearer ${this.token}`,
        Refresh: this.refreshToken,
        withCredentials: true,
      },
    });
    /*
        Authorization: `Bearer ${this.token}`,
        Refresh: this.refreshToken,
    */
  }

  public async getMyProfile() {
    try {
      let response = await this.apiExecutorJson?.get(`/users/me/profile`);
      this.myProfile = response!.data;
      console.log(this.myProfile);
    } catch {}
  }

  public async getLastMessages(moimId: string) {
    try {
      let response = await this.apiExecutor?.get(`/chat-history/${moimId}`);
      console.log(response!.data);
      return response!.data;
    } catch {
      /** dummy data */
      return false;
    }
  }

  public async getChattingParticipants(moimId: string) {
    try {
      let response = await this.apiExecutorJson?.get(`/chat/${moimId}/users`);
      return response!.data;
    } catch {
      return false;
    }
  }

  public async getTicket(moimId: string) {
    try {
      let response = await this.apiExecutor?.get(`/tickets/${moimId}`);
      console.log(response!.data);
      return response!.data;
    } catch {
      /** dummy data */
      return false;
    }
  }

  public async createTicket(moimId: string, template: string) {
    try {
      console.log('post');
      let response = await this.apiExecutorJson?.post(
        `/tickets/${moimId}?template=${template}`
      );
      return response!.data;
    } catch {
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

  public async editTicket(moimId: string, body: FormData) {
    try {
      let response = await this.apiExecutor?.put(`/tickets/${moimId}`, body);
      return response!.data;
    } catch (error) {
      console.log(error);
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

  public async uploadTicketImages(moimId: string, body: FormData) {
    try {
      let response = await this.apiExecutor?.post(
        `/tickets/${moimId}/image`,
        body
      );
      return response!.data;
    } catch (error) {
      console.log(error);
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

  public async createMoimReview(moimId: string, body: any) {
    try {
      let response = await this.apiExecutorJson?.post(
        `/diary/${moimId}/info`,
        body
      );
      return response!.data;
    } catch {
      return false;
    }
  }

  public async getMoimReview(moimId: string) {
    try {
      let response = await this.apiExecutorJson?.get(`/diary/${moimId}/info`);
      console.log(response!.data);
      return response!.data;
    } catch {
      return false;
    }
  }

  public async getDiary(moimId: string) {
    try {
      let response = await this.apiExecutor?.get(`/diary/${moimId}`);
      return JSON.parse(response!.data.resultJSON);
    } catch {
      /** dummy data */
      return false;
    }
  }

  public async createDiary(moimId: string, template: string) {
    try {
      let response = await this.apiExecutorJson?.post(
        `/diary/${moimId}?template=${template}`
      );
      return response!.data;
    } catch (e) {
      console.log(e);
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

  public async editDiary(moimId: string, body: FormData) {
    try {
      let response = await this.apiExecutor?.put(`/diary/${moimId}`, body);
      return response!.data;
    } catch (error) {
      console.log(error);
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

  public async uploadDiaryImages(moimId: string, body: FormData) {
    try {
      let response = await this.apiExecutor?.post(
        `/diary/${moimId}/image`,
        body
      );
      return response!.data;
    } catch (error) {
      console.log(error);
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

  public async uploadGeneralImage(body: FormData) {
    try {
      let response = await this.apiExecutor?.post(
        `/image-upload/general`,
        body
      );
      return response!.data;
    } catch (error) {
      console.log(error);
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

  public async getBarcode(moimId: string) {
    try {
      let response = await this.apiExecutorJson?.get(`/tickets/${moimId}/link`);
      return response!.data;
    } catch (error) {
      return 'asdfo7a809sd7fwae9089iafa';
    }
  }

  public async checkQR(moimId: string, qrString: string) {
    try {
      let body = { qrKey: qrString };
      let response = await this.apiExecutorJson?.post(
        `/tickets/${moimId}/link`,
        body
      );
      return response!.data;
    } catch {}
  }

  public async changeAttendanceStatus(
    moimId: string,
    userId: number,
    status: boolean = false
  ) {
    try {
      let response = await this.apiExecutorJson?.post(
        `/moims/${moimId}/attendance/${userId}`
      );
      return response!.data;
    } catch {}
  }

  public async getJoiningUsers(moimId: string) {
    try {
      let response = await this.apiExecutorJson?.get(`/moims/${moimId}/users`);
      return response!.data;
    } catch {}
  }

  public async getMoimInfo(moimId: string) {
    try {
      let response = await this.apiExecutorJson?.get(`/moims/${moimId}`);
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }

  public async getMoims(params: string = '') {
    try {
      let url =
        params === ''
          ? '/moims?size=1000&isEnd=false'
          : `/moims?isEnd=false&category=${params}&size=1000`;
      let response = await this.apiExecutorJson?.get(url);
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }

  public async getRunningMoims() {
    try {
      let url = `/moims?isParticipant=true&isEnd=false&size=100`;
      let response = await this.apiExecutorJson?.get(url);
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }

  public async getEndedMoims() {
    try {
      let url = `/moims?isParticipant=true&isEnd=true&size=100`;
      let response = await this.apiExecutorJson?.get(url);
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }

  public async joinMoim(moimId: string) {
    try {
      let response = await this.apiExecutorJson?.post(`moims/${moimId}/users`);
      return response?.data;
    } catch (e) {
      return false;
    }
  }

  public async checkParticipant(moimId: string) {
    try {
      let response = await this.apiExecutorJson?.get(
        `moims/${moimId}/participant`
      );
      return response!.data;
    } catch (e: any) {
      return false;
    }
  }

  public async checkCreator(moimId: string) {
    try {
      let response = await this.apiExecutorJson?.get(`moims/${moimId}/creator`);
      return response!.data;
    } catch (e: any) {
      return false;
    }
  }

  public async getProfile() {
    try {
      let response = await this.apiExecutorJson?.get(`/users/me/profile`);
      return response!.data;
    } catch (e) {
      return false;
    }
  }

  public async getIsRegister() {
    try {
      let response = await this.apiExecutorJson?.get(`/users/me/signup-verify`);
      return response!.data.isRegistered;
    } catch (e) {
      return false;
    }
  }

  public async createMeeting(body: any) {
    try {
      let response = await this.apiExecutorJson?.post(`/moims`, body);
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }

  public async registerUser(body: RegisterUserInfo) {
    try {
      let response = await this.apiExecutorJson?.post('/auth/signup', body);
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }
}
