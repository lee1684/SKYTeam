import axios, { AxiosInstance } from 'axios';
import { SsalonConfigService } from './ssalon-config.service';
import { Injectable } from '@angular/core';

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
  public tokens = {
    access:
      'eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsInVzZXJuYW1lIjoibmF2ZXIgbHphV19oUmprc1kzZXo1NUtJckpXdE9mMk1qTi1GZzJJbUF5SXBPOFNlcyIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MTUyNDA4NjUsImV4cCI6MTcxNTMyNzI2NX0.PL-LWei2pL-NBhpxDFGkyCjLAgXYYhb4hWk7wNCJY24',
    refresh:
      'eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6InJlZnJlc2giLCJ1c2VybmFtZSI6Im5hdmVyIGx6YVdfaFJqa3NZM2V6NTVLSXJKV3RPZjJNak4tRmcySW1BeUlwTzhTZXMiLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzE1MjQwODY1LCJleHAiOjE3MTUzMjcyNjV9.Ow5FwPpGzlumSzO2ELoCuYRqqg3bu73K-jt9aqf3nb4',
  };
  public _token: string = this.tokens.access;
  public refreshToken: string = this.tokens.refresh;

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

  public async getTicket() {
    try {
      let response = await this.apiExecutor?.get(
        `/tickets/${this._ssalonConfigService.MOIM_ID}`
      );
      return response!.data;
    } catch {
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

  public async editTicket(body: FormData) {
    try {
      let response = await this.apiExecutor?.put(
        `/tickets/${this._ssalonConfigService.MOIM_ID}`,
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

  public async getBarcode() {
    try {
      let response = await this.apiExecutorJson?.get(
        `/tickets/${this._ssalonConfigService.MOIM_ID}/link`
      );
      return response!.data;
    } catch (error) {
      return 'asdfo7a809sd7fwae9089iafa';
    }
  }

  public async checkQR(qrString: string) {
    try {
      let body = { qrKey: qrString };
      let response = await this.apiExecutorJson?.post(
        `/tickets/${this._ssalonConfigService.MOIM_ID}/link`,
        body
      );
      return response!.data;
    } catch {}
  }

  public async changeAttendanceStatus(userId: number, status: boolean = false) {
    try {
      let response = await this.apiExecutorJson?.post(
        `/moims/${this._ssalonConfigService.MOIM_ID}/attendance/${userId}`
      );
      return response!.data;
    } catch {}
  }

  public async getJoiningUsers() {
    try {
      let response = await this.apiExecutorJson?.get(
        `/moims/${this._ssalonConfigService.MOIM_ID}/users`
      );
      return response!.data;
    } catch {}
  }
}
