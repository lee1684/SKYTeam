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

@Injectable({
  providedIn: 'root',
})
export class ApiExecutorService {
  public apiExecutor: AxiosInstance | null = null;
  public apiExecutorJson: AxiosInstance | null = null;
  public apiURL: string = 'https://ssalon.co.kr/api';
  //public apiURL: string = 'http://localhost:8080/api';
  public tokens = {};
  private _token: string =
    'eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsInVzZXJuYW1lIjoibmF2ZXIgbHphV19oUmprc1kzZXo1NUtJckpXdE9mMk1qTi1GZzJJbUF5SXBPOFNlcyIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MTYxODMwMjEsImV4cCI6MTcxNjI2OTQyMX0.o1DD1nZjJN08Aav5OGVdRGYzdjLuXYAfRQWx0FxJ-6Q';
  public refreshToken: string = '';

  constructor(private _ssalonConfigService: SsalonConfigService) {
    this.initApiExecutor();
  }

  public setToken(token: string) {
    this._token = token;
  }

  private initApiExecutor() {
    this.apiExecutor = axios.create({
      baseURL: this.apiURL,
      headers: {
        'Content-Type': 'multipart/form-data',
        'Access-Control-Allow-Origin': '*',
      },
    });
    this.apiExecutorJson = axios.create({
      baseURL: this.apiURL,
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        Authorization: `Bearer ${this._token}`,
        Refresh: this.refreshToken,
      },
    });
    /*
        Authorization: `Bearer ${this.token}`,
        Refresh: this.refreshToken,
    */
  }

  public async getTicket(moimId: string) {
    try {
      let response = await this.apiExecutor?.get(`/tickets/${moimId}`);
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

  public async getDiary() {
    try {
      let response = await this.apiExecutor?.post(
        `/diary/${this._ssalonConfigService.DIARY_ID}`
      );
      return response!.data;
    } catch {
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

  public async editDiary(diaryId: string) {}

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
      let url = params === '' ? 'moims' : `moims?category=${params}`;
      console.log(url);
      let response = await this.apiExecutorJson?.get(url);
      return response!.data;
    } catch (e) {
      console.log(e);
    }
  }

  public async getJoinedMoims() {}

  public async getCreatedMoims() {}

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
