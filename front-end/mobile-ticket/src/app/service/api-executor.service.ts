import axios, { AxiosInstance } from 'axios';
import { DecorationInfo, SsalonConfigService } from './ssalon-config.service';
import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

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
  //public apiURL: string = 'http://3.34.0.190:8080/api';
  public apiURL: string = 'http://localhost:8080/api';
  public tokens = {
    access:
      'eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsInVzZXJuYW1lIjoibmF2ZXIgbHphV19oUmprc1kzZXo1NUtJckpXdE9mMk1qTi1GZzJJbUF5SXBPOFNlcyIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MTUwNzc0MzcsImV4cCI6MTcxNTE2MzgzN30.TurCJ_OEBy3kuNGcZFfBrtQb2ejySywncC0vrFipRkQ',
    refresh:
      'eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6InJlZnJlc2giLCJ1c2VybmFtZSI6Im5hdmVyIGx6YVdfaFJqa3NZM2V6NTVLSXJKV3RPZjJNak4tRmcySW1BeUlwTzhTZXMiLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzE1MDc3NDM3LCJleHAiOjE3MTUxNjM4Mzd9.UnuTh9J9kcUKn1FnO_VEnzG25gBkRoqL4ZWwqhT07dU',
  };
  private _token: string = this.tokens.access;
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
      console.log('As');
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
      console.log(typeof response!.data);
      return response!.data;
    } catch (error) {
      console.log(error);
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
}
