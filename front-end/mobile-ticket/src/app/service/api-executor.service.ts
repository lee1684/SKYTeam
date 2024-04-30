import { Injectable } from '@angular/core';
import axios, { AxiosInstance } from 'axios';
import { SsalonConfigService } from './ssalon-config.service';
@Injectable({
  providedIn: 'root',
})
export class ApiExecutorService {
  public apiExecutor: AxiosInstance | null = null;
  public apiURL: string = 'http://3.34.0.190:8080/api';
  public token: string =
    'eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6ImFjY2VzcyIsInVzZXJuYW1lIjoibmF2ZXIgbHphV19oUmprc1kzZXo1NUtJckpXdE9mMk1qTi1GZzJJbUF5SXBPOFNlcyIsInJvbGUiOiJST0xFX1VTRVIiLCJpYXQiOjE3MTQ0NTY0OTEsImV4cCI6MTcxNDQ1NzA5MX0.cyPRGFhpyZ_UKN3iarhJYoOVsBe-LuEpLOR1pjAFBVE';

  constructor(private _ssalonConfigService: SsalonConfigService) {
    this.initApiExecutor();
  }

  private initApiExecutor() {
    this.apiExecutor = axios.create({
      baseURL: this.apiURL,
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${this.token}`,
        Refresh: `eyJhbGciOiJIUzI1NiJ9.eyJjYXRlZ29yeSI6InJlZnJlc2giLCJ1c2VybmFtZSI6Im5hdmVyIGx6YVdfaFJqa3NZM2V6NTVLSXJKV3RPZjJNak4tRmcySW1BeUlwTzhTZXMiLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzE0NDU2NDkxLCJleHAiOjE3MTQ1NDI4OTF9.jNpTX6SSj-SemyEuoY0Ixrn3oJsogj0BZhMpTwN70Qg`,
      },
    });
  }

  public async getTicket() {
    try {
      let response = await this.apiExecutor?.post(
        `/tickets/${this._ssalonConfigService.MOIM_ID}`
      );
      return response!.data;
    } catch {
      /** dummy data */
      let tempJson = await axios.get('assets/decoration.json');
      return tempJson.data;
    }
  }

  public async editTicket(moimId: string) {}

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
}
